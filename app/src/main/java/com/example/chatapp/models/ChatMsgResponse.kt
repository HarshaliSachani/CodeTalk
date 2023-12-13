package com.example.chatapp.models


data class ChatMsgResponse(
    val message: ArrayList<ChatMsg>
)

data class ChatMsg(
    val id: Int,
    val userId: String,
    val username: String,
    val roomId: String,
    val message: String,
    val isAdmin: Boolean,
    val profile: String,
    val time: Long
)