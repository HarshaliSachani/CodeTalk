package com.example.chatapp.utils

object Constants {

//    const val SOCKET_BASE_URL = "http://192.168.249.40:3000"
//    const val API_BASE_URL = "http://192.168.249.40:3030"

    var SOCKET_BASE_URL = "https://chat-socketv1.herokuapp.com/"
    var API_BASE_URL = "https://chat-appdatabase.herokuapp.com/"

    const val ADMIN_LOGIN = "/admin/login"
    const val USER_LOGIN = "/user/login"
    const val CREATE_GROUP = "/create/group"
    const val ADD_USER = "add/user"
    const val DELETE_GROUP = "/delete/group"
    const val ADMIN_GROUP = "admin/group"  // fetch all group list for admin
    const val USER_GROUP = "user/group"  // fetch all group list for user
    const val GROUP_USER = "/group/user"  // fetch all user list for group
    const val DELETE_USER = "/delete/user"  // delete user from group
    const val GENERATE_OTP = "/generate-otp"  // generate otp for forget password
    const val FORGET_PASSWORD = "/forget-password"  // update new pwd when forget pwd
    const val CLEAR_CHAT_HISTORY = "/clear/history"  // clear group history


    const val EMAIL_ID = "user_name"
    const val PASSWORD = "password"
    const val GROUP = "group"
    const val ROOM = "room"
    const val ID = "id"
    const val USER_NAME = "username"
    const val MESSAGE = "message"
    const val IS_ADMIN = "isAdmin"
    const val PROFILE_INDEX = "profile"
    const val CREATED_AT = "createAt"
    const val GROUP_NAME = "groupName"
    const val GROUP_ICON = "groupIcon"

    const val OTP = "OTP"

    const val LOADER_DELAY = 2000L

}