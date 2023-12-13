package com.example.chatapp.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ListItemGroupIconBinding

class GroupIconAdapter(
    val activity: Activity,
    val onGroupClick: (index: Int) -> Unit
) : ListAdapter<Int, GroupIconAdapter.GroupIconHolder>(object : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

}) {

    var selectedIndex = -1

    fun setGroupIconSelection(index: Int) {
        if (selectedIndex != -1) notifyItemChanged(selectedIndex, 200)
        selectedIndex = index
        notifyItemChanged(selectedIndex, 100)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupIconHolder {
        return GroupIconHolder(ListItemGroupIconBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupIconHolder, position: Int, payloads: MutableList<Any>) {
        when {
            payloads.isEmpty() -> super.onBindViewHolder(holder, position, payloads)
            payloads[0] == 100 -> holder.setSelection(true)
            payloads[0] == 200 -> holder.setSelection(false)
        }
    }

    override fun onBindViewHolder(holder: GroupIconHolder, position: Int) {
        holder.setData(currentList[position])
    }

    inner class GroupIconHolder(val binding: ListItemGroupIconBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setData(groupIcon: Int) {
            Log.e("TAG", "setData: groupIcon=$groupIcon")
            Glide.with(binding.imgGroupProfile)
                .asDrawable()
                .load(groupIcon)
                .into(binding.imgGroupProfile)

            binding.groupCardLayout.setOnClickListener { onGroupClick(adapterPosition) }

        }

        fun setSelection(isSelected: Boolean) {
            binding.viewImgBorder.isVisible = isSelected
        }

    }


}