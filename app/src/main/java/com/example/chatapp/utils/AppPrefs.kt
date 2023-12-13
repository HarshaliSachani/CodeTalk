package com.example.chatapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppPrefs {

    private val TAG = javaClass.simpleName
    private lateinit var preferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor

    fun initiate(context: Context) {
        preferences = context.getSharedPreferences("ChatApp_Pref", Context.MODE_PRIVATE)
        prefEditor = preferences.edit()
    }

    fun saveValue(keyConstant: PrefConstant, value: Any) {
        when (value) {
            is Boolean -> prefEditor.putBoolean(keyConstant.name, value)
            is String -> prefEditor.putString(keyConstant.name, value)
            is Int -> prefEditor.putInt(keyConstant.name, value)
            is Long -> prefEditor.putLong(keyConstant.name, value)
            is Float -> prefEditor.putFloat(keyConstant.name, value)
        }
        prefEditor.commit()
    }

    fun getBoolean(keyConstant: PrefConstant) = preferences.getBoolean(keyConstant.name, false)
    fun getString(keyConstant: PrefConstant): String {
        return preferences.getString(keyConstant.name, "") ?: ""
    }

    fun getInt(keyConstant: PrefConstant) = preferences.getInt(keyConstant.name, 0)
    fun getLong(keyConstant: PrefConstant) = preferences.getLong(keyConstant.name, 0L)
    fun getFloat(keyConstant: PrefConstant) = preferences.getFloat(keyConstant.name, 0f)


    fun clearPref() {
        prefEditor.clear()
        prefEditor.commit()
    }
}