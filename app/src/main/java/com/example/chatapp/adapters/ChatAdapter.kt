package com.example.chatapp.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.ListItemChatLeftBinding
import com.example.chatapp.databinding.ListItemChatRightBinding
import com.example.chatapp.extensions.convertToTime
import com.example.chatapp.extensions.getUserName
import com.example.chatapp.models.ChatMsg

class ChatAdapter(
    private val activity: Activity,
    private val selfUserId: String,
    val msgList: ArrayList<ChatMsg>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()/*  ListAdapter<ChatMsg, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<ChatMsg>() {
    override fun areItemsTheSame(oldItem: ChatMsg, newItem: ChatMsg): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ChatMsg, newItem: ChatMsg): Boolean {
        return oldItem == newItem
    }

})*/ {

    private val MSG_LEFT = 0
    private val MSG_RIGHT = 1
    private var isSameUser = true
    public var oldUserId = ""


    fun addNewMessage(chatMsg: ChatMsg) {
        msgList.add(chatMsg)
        notifyItemInserted(msgList.size - 1)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        MSG_LEFT -> ChatLeftHolder(ListItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        MSG_RIGHT -> ChatRightHolder(ListItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else -> ChatRightHolder(ListItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
//        Log.e("TAG", "onCreate: set adapter msg= playload vh=${payloads}")
        when {
            payloads.isEmpty() -> super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        Log.e("TAG", "bindview holder==========${holder}")
        if (holder is ChatLeftHolder) holder.setMessageData(msgList[position])
        else if (holder is ChatRightHolder) holder.setMessageData(msgList[position])
    }

    override fun getItemViewType(position: Int): Int {
//        Log.e("TAG", "bindview holder==========userId=${currentList[position].userId}   prefId=${AppPrefs.getString(PrefConstant.id)}")
        return when (msgList[position].userId) {
            selfUserId -> MSG_RIGHT
            else -> MSG_LEFT
        }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    inner class ChatLeftHolder(private val binding: ListItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setMessageData(chatMsg: ChatMsg) {
            binding.txtLeftMsg.text = chatMsg.message
            binding.txtMsgTime.text = chatMsg.time.convertToTime()
            Glide.with(binding.imgProfile)
                .asDrawable()
                .load(if (chatMsg.isAdmin) R.drawable.admin_profile else activity.resources.getIdentifier("profile_" + chatMsg.profile, "drawable", activity.packageName))
                .into(binding.imgProfile)

            binding.nameView.isVisible = isSameUser
            binding.txtUserName.isVisible = isSameUser
            if (isSameUser) binding.txtUserName.text = chatMsg.username.getUserName()
            isSameUser = oldUserId == chatMsg.userId
            oldUserId = chatMsg.userId


        }
    }

    class ChatRightHolder(private val binding: ListItemChatRightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setMessageData(chatMsg: ChatMsg) {
            binding.txtRightMsg.text = chatMsg.message
            binding.txtMsgTime.text = chatMsg.time.convertToTime()
        }
    }


}