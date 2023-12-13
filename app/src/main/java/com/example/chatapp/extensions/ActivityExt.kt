package com.example.chatapp.extensions

import android.app.Activity
import android.content.Intent
import com.example.chatapp.activities.*
import com.example.chatapp.utils.Constants


fun Activity.startIntroActivity() {
    startActivity(Intent(this, IntroActivity::class.java))
}

fun Activity.startLoginActivity() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.startForgetPasswordActivity() {
    startActivity(Intent(this,ForgetPasswordActivity::class.java))
}

fun Activity.startDashboardActivity() {
    val i = Intent(this, DashboardActivity::class.java)
    startActivity(i)
}

fun Activity.startChatActivity(groupName: String, groupIcon: String) {
    val i = Intent(this, ChatActivity::class.java)
    i.putExtra(Constants.GROUP_NAME, groupName)
    i.putExtra(Constants.GROUP_ICON, groupIcon)
    startActivity(i)
}


