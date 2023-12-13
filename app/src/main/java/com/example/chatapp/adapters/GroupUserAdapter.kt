package com.example.chatapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ListItemUserBinding
import com.example.chatapp.models.GroupUser

class GroupUserAdapter(
    val activity: Activity,
    val onGroupClick: (groupData: GroupUser) -> Unit
) : ListAdapter<GroupUser, GroupUserAdapter.GroupHolder>(object : DiffUtil.ItemCallback<GroupUser>() {
    override fun areItemsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
        return oldItem.user_name == newItem.user_name
    }

    override fun areContentsTheSame(oldItem: GroupUser, newItem: GroupUser): Boolean {
        return oldItem == newItem
    }

}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        return GroupHolder(ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.setData(currentList[position])
    }

    inner class GroupHolder(val binding: ListItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(groupUser: GroupUser) {
            binding.txtGroupUserName.text = groupUser.name
            Glide.with(binding.imgGroupUserProfile)
                .asDrawable()
                .load(activity.resources.getIdentifier("profile_" + groupUser.user_profile, "drawable", activity.packageName))
                .into(binding.imgGroupUserProfile)

            binding.groupCardLayout.setOnClickListener { onGroupClick(currentList[adapterPosition]) }

        }

    }


}