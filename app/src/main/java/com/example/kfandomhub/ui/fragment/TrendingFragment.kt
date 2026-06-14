package com.example.kfandomhub.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kfandomhub.R
import com.example.kfandomhub.data.model.MusicRadarCategory
import com.example.kfandomhub.data.model.TrendingItem
import com.example.kfandomhub.data.repository.KpopRepository
import com.example.kfandomhub.ui.adapter.TrendingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TrendingFragment : Fragment() {

    @Inject
    lateinit var kpopRepository: KpopRepository

    private lateinit var rvTrending: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: TrendingAdapter
    private lateinit var tvEmptyState: TextView
    private var allTrending: List<TrendingItem> = emptyList()
    private val categoryCache = mutableMapOf<MusicRadarCategory, List<TrendingItem>>()
    private val nextPageTokens = mutableMapOf<MusicRadarCategory, String?>()
    private val exhaustedCategories = mutableSetOf<MusicRadarCategory>()
    private var searchQuery: String = ""
    private var selectedCategory = MusicRadarCategory.NEW_RELEASE
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTrending = view.findViewById(R.id.rvTrending)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmptyState = view.findViewById(R.id.tvEmptyState)
        val search = view.findViewById<EditText>(R.id.etSearchTrending)

        adapter = TrendingAdapter { item ->
            openYouTube(item)
        }
        rvTrending.layoutManager = LinearLayoutManager(context)
        rvTrending.adapter = adapter
        rvTrending.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && !recyclerView.canScrollVertically(1)) {
                    fetchRadarData(append = true)
                }
            }
        })

        search.addTextChangedListener { text ->
            searchQuery = text?.toString().orEmpty()
            applyFilterAndSort()
        }

        setupCategoryFilters(view)
        fetchRadarData()
    }

    private fun setupCategoryFilters(view: View) {
        val chips = listOf(
            view.findViewById<TextView>(R.id.chipNewRelease) to MusicRadarCategory.NEW_RELEASE,
            view.findViewById<TextView>(R.id.chipTrendingMv) to MusicRadarCategory.TRENDING_MV,
            view.findViewById<TextView>(R.id.chipMostViewed) to MusicRadarCategory.MOST_VIEWED_MV
        )

        fun render() {
            chips.forEach { (chip, category) ->
                val active = selectedCategory == category
                chip.setBackgroundResource(if (active) R.drawable.background_chip_selected else R.drawable.background_chip)
                chip.setTextColor(resources.getColor(if (active) android.R.color.white else R.color.text_secondary, null))
            }
        }

        chips.forEach { (chip, category) ->
            chip.setOnClickListener {
                selectedCategory = category
                render()
                fetchRadarData(append = false)
            }
        }
        render()
    }

    private fun fetchRadarData(append: Boolean = false) {
        if (isLoading) return
        val category = selectedCategory
        if (append && exhaustedCategories.contains(category)) return

        if (!append && categoryCache.containsKey(category)) {
            val cached = categoryCache[category].orEmpty()
            allTrending = cached
            applyFilterAndSort()
            if (cached.isNotEmpty() || exhaustedCategories.contains(category)) return
        }

        if (!append) {
            allTrending = emptyList()
            adapter.submitList(emptyList())
        }

        isLoading = true
        progressBar.visibility = View.VISIBLE
        tvEmptyState.visibility = View.GONE
        val token = if (append) nextPageTokens[category] else null
        viewLifecycleOwner.lifecycleScope.launch {
            val page = kpopRepository.getMusicRadar(category, token)
            val existing = if (append) categoryCache[category].orEmpty() else emptyList()
            val merged = (existing + page.items).distinctBy { it.videoId }

            categoryCache[category] = merged
            nextPageTokens[category] = page.nextPageToken
            if (page.nextPageToken == null) {
                exhaustedCategories.add(category)
            } else {
                exhaustedCategories.remove(category)
            }

            isLoading = false
            progressBar.visibility = View.GONE
            if (category != selectedCategory) return@launch

            allTrending = merged
            if (merged.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    if (kpopRepository.hasYouTubeApiKey()) {
                        "YouTube MV data is unavailable"
                    } else {
                        "Add YOUTUBE_API_KEY in local.properties"
                    },
                    Toast.LENGTH_SHORT
                ).show()
            }
            applyFilterAndSort()
        }
    }

    private fun applyFilterAndSort() {
        val query = searchQuery.trim()
        val filtered = allTrending.filter { item ->
            query.isBlank() ||
                item.title.contains(query, ignoreCase = true) ||
                item.groupName.contains(query, ignoreCase = true)
        }

        val sorted = when (selectedCategory) {
            MusicRadarCategory.NEW_RELEASE -> filtered.sortedByDescending { it.publishedAtMillis }
            MusicRadarCategory.TRENDING_MV -> filtered
            MusicRadarCategory.MOST_VIEWED_MV -> filtered.sortedByDescending { it.viewCount }
        }

        tvEmptyState.visibility = if (sorted.isEmpty() && progressBar.visibility != View.VISIBLE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        adapter.submitList(sorted)
    }

    private fun openYouTube(item: TrendingItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.youtubeUrl))
        runCatching {
            startActivity(intent)
        }.onFailure {
            Toast.makeText(requireContext(), "Unable to open YouTube", Toast.LENGTH_SHORT).show()
        }
    }
}
