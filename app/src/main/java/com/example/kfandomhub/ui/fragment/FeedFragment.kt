package com.example.kfandomhub.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.example.kfandomhub.R
import com.example.kfandomhub.data.database.PostDao
import com.example.kfandomhub.ui.adapter.PostAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var postDao: PostDao

    private lateinit var rv: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var emptyText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.findViewById(R.id.rvPost)
        emptyText = view.findViewById(R.id.tvEmptyFeed)
        progressBar = view.findViewById(R.id.progressBar)
        rv.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostAdapter(postDao)
        rv.adapter = adapter
        
        loadPosts()
    }

    private fun loadPosts() {
        viewLifecycleOwner.lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
            val posts = postDao.getAllPosts()
            progressBar.visibility = View.GONE
            emptyText.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(posts)
        }
    }
}
