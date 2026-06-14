package com.example.kfandomhub.ui.adapter

import android.view.*
import android.widget.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kfandomhub.R
import com.example.kfandomhub.data.model.Member

class MemberAdapter(
    private val onClick: (Member) -> Unit
) : ListAdapter<Member, MemberAdapter.ViewHolder>(MemberDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgMember)
        val name: TextView = view.findViewById(R.id.tvMemberName)
        val pos: TextView = view.findViewById(R.id.tvPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name
        holder.pos.text = item.position
        Glide.with(holder.itemView.context)
            .load(item.photoUrl)
            .placeholder(R.drawable.ic_avatar_placeholder)
            .error(R.drawable.ic_avatar_placeholder)
            .circleCrop()
            .into(holder.img)

        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    private object MemberDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
            oldItem == newItem
    }
}
