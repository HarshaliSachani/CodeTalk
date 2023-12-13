package com.example.chatapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ListItemGroupBinding
import com.example.chatapp.models.Group

class GroupAdapter(
    val activity: Activity,
    val onGroupClick: (groupData: Group) -> Unit
) : ListAdapter<Group, GroupAdapter.GroupHolder>(object : DiffUtil.ItemCallback<Group>() {
    override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        return GroupHolder(ListItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.setData(currentList[position])
    }

    inner class GroupHolder(val binding: ListItemGroupBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(groupName: Group) {
            binding.txtGroupName.text = groupName.group_name
            Glide.with(binding.imgGroupProfile)
                .asDrawable()
                .load(activity.resources.getIdentifier("group_" + groupName.group_icon, "drawable", activity.packageName))
                .into(binding.imgGroupProfile)

            binding.groupCardLayout.setOnClickListener { onGroupClick(currentList[adapterPosition]) }

        }

    }


}