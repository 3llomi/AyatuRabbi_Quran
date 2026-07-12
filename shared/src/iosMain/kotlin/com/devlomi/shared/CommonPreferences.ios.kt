package com.devlomi.shared

import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue
import platform.Foundation.valueForKey

actual class CommonPreferences() {
    private val nsUserDefaults = NSUserDefaults.standardUserDefaults

    actual fun getString(key: String, defaultValue: String?): String? {
        return nsUserDefaults.valueForKey(key) as? String
    }

    actual fun putString(key: String, value: String) {
        nsUserDefaults.setValue(value, forKey = key)
    }

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return nsUserDefaults.valueForKey(key) as? Boolean ?: defaultValue
    }

    actual fun putBoolean(key: String, value: Boolean) {
        nsUserDefaults.setValue(value, forKey = key)
    }

    actual fun getInt(key: String, defaultValue: Int): Int {
        return nsUserDefaults.valueForKey(key) as? Int ?: defaultValue
    }

    actual fun putInt(key: String, value: Int) {
        nsUserDefaults.setValue(value, forKey = key)
    }

    actual fun getFloat(key: String, defaultValue: Float): Float {
        return nsUserDefaults.valueForKey(key) as? Float ?: defaultValue
    }

    actual fun putFloat(key: String, value: Float) {
        nsUserDefaults.setValue(value, forKey = key)
    }
    actual fun contains(key: String): Boolean {
        return nsUserDefaults.valueForKey(key) != null
    }
}