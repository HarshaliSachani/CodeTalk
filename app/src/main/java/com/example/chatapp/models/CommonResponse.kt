package com.example.chatapp.models

data class CommonResponse(
    val code: Int,
    val title: String,
    val message: String,
    val success: Boolean,
    val data: String
)

