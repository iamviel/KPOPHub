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
import com.example.kfandomhub.data.model.Group

class GroupAdapter(
    private val onClick: (Group) -> Unit
) : ListAdapter<Group, GroupAdapter.ViewHolder>(GroupDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLogo: ImageView = view.findViewById(R.id.imgLogo)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvAgency: TextView = view.findViewById(R.id.tvAgency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.tvName.text = item.name
        holder.tvAgency.text = "${item.agency} - ${item.generation} - Debut ${item.debut}"

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.ic_image_placeholder)
            .error(R.drawable.ic_image_placeholder)
            .centerCrop()
            .into(holder.imgLogo)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    private object GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
        override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean =
            oldItem == newItem
    }
}
