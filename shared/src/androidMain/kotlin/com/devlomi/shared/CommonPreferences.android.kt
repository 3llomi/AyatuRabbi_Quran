package com.devlomi.shared

import android.content.SharedPreferences
import androidx.core.content.edit

actual class CommonPreferences(private val sharedPreferences: SharedPreferences) {
    actual fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    actual fun putString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    actual fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    actual fun putInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    actual fun getFloat(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue)
    }

    actual fun putFloat(key: String, value: Float) {
        sharedPreferences.edit { putFloat(key, value) }
    }
    actual fun contains(key: String): Boolean {
        return sharedPreferences.contains(key)
    }
}