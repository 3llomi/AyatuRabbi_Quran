package com.devlomi.shared

import platform.Foundation.NSUserDefaults

actual class CommonPreferences() {
    private val nsUserDefaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String, defaultValue: String?): String? {
        return nsUserDefaults.stringForKey(key) ?: defaultValue
    }

    actual fun putString(key: String, value: String) {
        nsUserDefaults.setObject(value, forKey = key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return if (contains(key)) nsUserDefaults.boolForKey(key) else defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        nsUserDefaults.setBool(value, forKey = key)
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return if (contains(key)) nsUserDefaults.integerForKey(key).toInt() else defaultValue
    }

    actual fun putInt(key: String, value: Int) {
        nsUserDefaults.setInteger(value.toLong(), forKey = key)
    }

    actual fun getFloat(key: String, defaultValue: Float): Float {
        return if (contains(key)) nsUserDefaults.floatForKey(key) else defaultValue
    }

    actual fun putFloat(key: String, value: Float) {
        nsUserDefaults.setFloat(value, forKey = key)
    }

    actual fun contains(key: String): Boolean {
        return nsUserDefaults.objectForKey(key) != null
    }
}