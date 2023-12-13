package com.example.chatapp.extensions

fun String.getUserName(): String {
    val char = this.split(" ")
    return char[0]
}