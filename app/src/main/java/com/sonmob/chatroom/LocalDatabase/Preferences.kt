package com.sonmob.chatroom.LocalDatabase

import android.content.Context
import android.content.SharedPreferences

class Preferences (var context: Context){

    var preferances: SharedPreferences
    private val PREFERENCES_KEY = "MY_PREFERENCES_KEY"

    init {
        preferances = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
    }

    fun saveData(key: String, value: String) {
        val editor = preferances.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String): String {
        return preferances.getString(key, "")!!
    }

    fun clearPreferances() {
        val editor = preferances.edit()
        editor.clear()
        editor.apply()
    }
}