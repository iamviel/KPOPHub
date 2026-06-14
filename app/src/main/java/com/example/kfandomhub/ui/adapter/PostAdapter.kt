package com.example.kfandomhub.ui.adapter

import android.net.Uri
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.kfandomhub.R
import com.example.kfandomhub.data.database.PostDao
import com.example.kfandomhub.data.model.Post
import com.example.kfandomhub.data.model.Comment
import com.example.kfandomhub.util.ProfileManager
import kotlinx.coroutines.launch

class PostAdapter(
    private val postDao: PostDao
) : ListAdapter<Post, PostAdapter.ViewHolder>(PostDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgPost)
        val imgAuthor: ImageView = view.findViewById(R.id.imgAuthor)
        val tvName: TextView = view.findViewById(R.id.tvAuthorName)
        val tvUser: TextView = view.findViewById(R.id.tvAuthorUsername)
        val tvDate: TextView = view.findViewById(R.id.tvPostDate)
        val tvTitle: TextView = view.findViewById(R.id.tvPostTitle)
        val tvCap: TextView = view.findViewById(R.id.tvCaption)
        val tvCommentCount: TextView = view.findViewById(R.id.tvViewComments)
        val tvCommentBadge: TextView = view.findViewById(R.id.tvCommentBadge)
        val tvTag: TextView = view.findViewById(R.id.tvPostTag)
        val tvCommentsPreview: TextView = view.findViewById(R.id.tvCommentsPreview)
        val tvLikeCount: TextView = view.findViewById(R.id.tvLikeCount)
        val btnLike: ImageView = view.findViewById(R.id.btnLike)
        val etComment: EditText = view.findViewById(R.id.etComment)
        val btnSend: TextView = view.findViewById(R.id.btnSendComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        val context = holder.itemView.context

        Glide.with(context)
            .load(post.imageUri)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_placeholder)
            .centerCrop()
            .into(holder.img)
        
        if (!post.authorImageUri.isNullOrEmpty()) {
            Glide.with(context)
                .load(Uri.parse(post.authorImageUri))
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .circleCrop()
                .into(holder.imgAuthor)
        } else {
            holder.imgAuthor.setImageResource(R.drawable.ic_avatar_placeholder)
        }
        
        holder.tvName.text = post.authorName
        holder.tvUser.text = "@${post.authorUsername}"
        holder.tvTitle.text = post.title
        holder.tvCap.text = post.caption
        holder.tvTag.text = post.tags.ifBlank { "#KPop" }
        holder.tvTag.visibility = if (holder.tvTag.text.isBlank()) View.GONE else View.VISIBLE
        renderLike(holder, post)
        
        val sdf = SimpleDateFormat("d MMM yyyy, HH:mm", Locale.ENGLISH)
        holder.tvDate.text = sdf.format(Date(post.timestamp))

        fun refreshComments() {
            holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                val comments = postDao.getCommentsForPost(post.id)
                val count = comments.size
                holder.tvCommentCount.text = "View all $count comments"
                holder.tvCommentBadge.text = count.toString()
                holder.tvCommentsPreview.visibility = if (comments.isEmpty()) View.GONE else View.VISIBLE
                holder.tvCommentsPreview.text = comments.takeLast(3).joinToString("\n") {
                    "${it.authorName}: ${it.text}"
                }
            }
        }
        
        refreshComments()

        holder.btnLike.setOnClickListener {
            val adapterPosition = holder.bindingAdapterPosition
            if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener

            val currentPost = getItem(adapterPosition)
            val currentlyLiked = currentPost.isLiked
            val nextLikeCount = if (currentlyLiked) {
                (currentPost.likeCount - 1).coerceAtLeast(0)
            } else {
                currentPost.likeCount + 1
            }
            val updated = currentPost.copy(
                likeCount = nextLikeCount,
                isLiked = !currentlyLiked
            )
            submitList(currentList.toMutableList().also { it[adapterPosition] = updated })
            renderLike(holder, updated)

            holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                postDao.updateLike(updated.id, updated.likeCount, updated.isLiked)
            }
        }

        holder.btnSend.setOnClickListener {
            val text = holder.etComment.text.toString()
            if (text.isNotBlank()) {
                val profile = ProfileManager(context)
                val comment = Comment(
                    postId = post.id,
                    authorName = profile.getName(),
                    text = text
                )
                
                holder.itemView.findViewTreeLifecycleOwner()?.lifecycleScope?.launch {
                    postDao.insertComment(comment)
                    holder.etComment.text.clear()
                    refreshComments()
                    Toast.makeText(context, "Comment added!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun renderLike(holder: ViewHolder, post: Post) {
        val context = holder.itemView.context
        holder.tvLikeCount.text = post.likeCount.toString()
        holder.btnLike.alpha = if (post.isLiked) 1f else 0.45f
        holder.btnLike.setColorFilter(
            ContextCompat.getColor(
                context,
                if (post.isLiked) R.color.secondary else R.color.text_secondary
            )
        )
    }

    private object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}
