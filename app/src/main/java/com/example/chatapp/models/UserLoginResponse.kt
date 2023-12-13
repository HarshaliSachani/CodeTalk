package com.example.chatapp.models

data class UserLoginResponse(
    val code: Int,
    val data: LoginData,
    val message: String,
    val success: Boolean
)


data class LoginData(
    val __v: Int,
    val _id: String,
    val group: ArrayList<Group>,
    val name: String,
    val user_name: String,
    val user_profile: String
)

data class Group(
    val group_name: String,
    val group_icon: String,
    val _id: String
)