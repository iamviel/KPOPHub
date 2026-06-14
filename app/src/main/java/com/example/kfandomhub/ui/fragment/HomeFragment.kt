package com.example.kfandomhub.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kfandomhub.R
import com.example.kfandomhub.data.model.Group
import com.example.kfandomhub.data.repository.KpopRepository
import com.example.kfandomhub.ui.adapter.GroupAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var kpopRepository: KpopRepository

    private lateinit var adapter: GroupAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyText: TextView
    private var allGroups: List<Group> = emptyList()
    private var selectedGeneration: String? = null
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvGroup)
        val search = view.findViewById<EditText>(R.id.etSearchGroups)
        progressBar = view.findViewById(R.id.progressBar)
        emptyText = view.findViewById(R.id.tvEmptyGroups)

        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = GroupAdapter { group ->
            openMembers(group)
        }
        rv.adapter = adapter

        setupGenerationFilters(view)
        search.addTextChangedListener { text ->
            searchQuery = text?.toString().orEmpty()
            applyFilters()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
            allGroups = kpopRepository.getGroups()
            progressBar.visibility = View.GONE
            applyFilters()
        }
    }

    private fun setupGenerationFilters(view: View) {
        val chips = listOf(
            view.findViewById<TextView>(R.id.chipAll) to null,
            view.findViewById<TextView>(R.id.chipFifth) to "5th Gen",
            view.findViewById<TextView>(R.id.chipFourth) to "4th Gen",
            view.findViewById<TextView>(R.id.chipThird) to "3rd Gen",
            view.findViewById<TextView>(R.id.chipSecond) to "2nd Gen",
            view.findViewById<TextView>(R.id.chipFirst) to "1st Gen"
        )

        fun render() {
            chips.forEach { (chip, generation) ->
                val active = selectedGeneration == generation
                chip.setBackgroundResource(if (active) R.drawable.background_chip_selected else R.drawable.background_chip)
                chip.setTextColor(resources.getColor(if (active) android.R.color.white else R.color.text_secondary, null))
            }
        }

        chips.forEach { (chip, generation) ->
            chip.setOnClickListener {
                selectedGeneration = generation
                render()
                applyFilters()
            }
        }
        render()
    }

    private fun applyFilters() {
        val query = searchQuery.trim()
        val filtered = allGroups.filter { group ->
            val matchesGeneration = selectedGeneration == null || group.generation == selectedGeneration
            val matchesSearch = query.isBlank() ||
                group.name.contains(query, ignoreCase = true) ||
                group.agency.contains(query, ignoreCase = true) ||
                group.fandomName.contains(query, ignoreCase = true)
            matchesGeneration && matchesSearch
        }

        adapter.submitList(filtered)
        emptyText.visibility = if (filtered.isEmpty() && progressBar.visibility != View.VISIBLE) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun openMembers(group: Group) {
        val fragment = MemberFragment()
        val bundle = Bundle()
        bundle.putInt("groupId", group.id)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
