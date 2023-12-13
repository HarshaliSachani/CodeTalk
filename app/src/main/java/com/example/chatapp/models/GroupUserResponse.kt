package com.example.chatapp.models

data class GroupUserResponse(
    val code: Int,
    val data: List<GroupUser>,
    val message: String,
    val success: Boolean
)

data class GroupUser(
    val name: String,
    val user_name: String,
    val user_profile: String
)