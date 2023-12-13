package com.example.chatapp.utils

import com.example.chatapp.models.Group

class AppData {

    var groupId = ""
    var isRefreshGroup = false

    companion object {
        private var appData: AppData? = null
        fun getInstance(): AppData {
            if (appData == null) appData = AppData()
            return appData!!
        }
    }


}