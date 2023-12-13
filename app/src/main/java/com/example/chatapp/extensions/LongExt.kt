package com.example.chatapp.extensions

import java.text.SimpleDateFormat
import java.util.*


fun Long.convertToTime(): String {
    val sdf = SimpleDateFormat("hh:mm aa", Locale.US)
    sdf.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
    return sdf.format(this)
}