package com.example.kfandomhub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kfandomhub.R
import com.example.kfandomhub.data.model.TrendingItem

class TrendingAdapter(
    private val onClick: (TrendingItem) -> Unit
) :
    ListAdapter<TrendingItem, TrendingAdapter.ViewHolder>(TrendingDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgThumbnail: ImageView = view.findViewById(R.id.imgVideoThumbnail)
        val tvTitle: TextView = view.findViewById(R.id.tvVideoTitle)
        val tvGroup: TextView = view.findViewById(R.id.tvGroupName)
        val tvMeta: TextView = view.findViewById(R.id.tvVideoMeta)
        val tvViews: TextView = view.findViewById(R.id.tvViews)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trending, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvTitle.text = item.title
        holder.tvGroup.text = item.groupName
        holder.tvMeta.text = item.releasedAgo
        holder.tvViews.text = item.viewText

        Glide.with(holder.itemView.context)
            .load(item.thumbnailUrl)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_placeholder)
            .centerCrop()
            .into(holder.imgThumbnail)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    private object TrendingDiffCallback : DiffUtil.ItemCallback<TrendingItem>() {
        override fun areItemsTheSame(oldItem: TrendingItem, newItem: TrendingItem): Boolean =
            oldItem.videoId == newItem.videoId

        override fun areContentsTheSame(oldItem: TrendingItem, newItem: TrendingItem): Boolean =
            oldItem == newItem
    }
}
