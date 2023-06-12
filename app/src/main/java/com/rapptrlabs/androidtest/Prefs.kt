package com.rapptrlabs.androidtest

import android.content.Context

class Prefs(var context: Context) {
    val preferences = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    companion object {
        const val REQUEST_DURATION = "com.rapptrlabs.androidtest.Prefs.REQUEST_DURATION"
    }

    fun setLongValue(key: String, value: Long): Prefs {
        preferences.edit().putLong(key, value).apply()
        return this
    }

    fun getLongValue(key: String): Long {
        return preferences.getLong(key, 0L)
    }
}