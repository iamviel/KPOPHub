package com.example.kfandomhub.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.kfandomhub.R
import com.example.kfandomhub.data.repository.KpopRepository
import com.example.kfandomhub.ui.adapter.MemberAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MemberFragment : Fragment() {

    @Inject
    lateinit var kpopRepository: KpopRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val rv = view.findViewById<RecyclerView>(R.id.rvMember)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val groupId = arguments?.getInt("groupId") ?: 1
        val adapter = MemberAdapter { member ->
            val fragment = DetailFragment()
            val bundle = Bundle()
            bundle.putString("name", member.name)
            bundle.putString("position", member.position)
            bundle.putString("photoUrl", member.photoUrl)
            bundle.putString("stageName", member.stageName)
            bundle.putString("birthName", member.birthName)
            bundle.putString("birthday", member.birthday)
            bundle.putString("zodiacSign", member.zodiacSign)
            bundle.putString("chineseZodiacSign", member.chineseZodiacSign)
            bundle.putString("height", member.height)
            bundle.putString("weight", member.weight)
            bundle.putString("bloodType", member.bloodType)
            bundle.putString("mbtiType", member.mbtiType)
            bundle.putString("nationality", member.nationality)
            bundle.putString("birthplace", member.birthplace)
            bundle.putString("instagram", member.instagram)
            bundle.putString("sourceUrl", member.sourceUrl)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        }

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            val list = kpopRepository.getMembers(groupId)
            progressBar.visibility = View.GONE
            adapter.submitList(list)
        }
    }
}
