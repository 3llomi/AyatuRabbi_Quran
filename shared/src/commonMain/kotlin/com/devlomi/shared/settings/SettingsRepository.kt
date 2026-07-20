package com.devlomi.shared.settings

import com.devlomi.shared.ColorItem
import com.devlomi.shared.CommonPreferences


class SettingsRepository(private val sharedPreferences: CommonPreferences) {

    fun deviceWidth(): Int {
        return sharedPreferences.getInt("width", 1260)
    }

    fun hasDownloadedFiles(): Boolean = sharedPreferences.getBoolean("files_downloaded", false)
    fun saveDeviceWidth(properWidth: Int) {
        if (sharedPreferences.contains("width")) return
        sharedPreferences.putInt("width", properWidth)
    }

    fun setDownloadFinished(b: Boolean) {
        sharedPreferences.putBoolean("files_downloaded", b)
    }


    fun saveBackgroundColor(backgroundColorName: String) {
        sharedPreferences.putString("bgColorName", backgroundColorName)
    }

    fun getBackgroundColorName(): String {
        return sharedPreferences.getString("bgColorName", ColorItem.DKBLUE.name)
            ?: ColorItem.DKBLUE.name
    }

    fun saveCurrentIndex(currentIndex: Int) {
        sharedPreferences.putInt("current_index", currentIndex)
    }

    fun getCurrentIndex() = sharedPreferences.getInt("current_index", 0)

    fun preventScreenlock() = sharedPreferences.getBoolean("prevent_screen_lock", true)
    fun setPreventScreenlock(boolean: Boolean) {
        sharedPreferences.putBoolean("prevent_screen_lock", boolean)
    }

    fun getScale(): Float {
        return sharedPreferences.getFloat("scale", 1.0f)
    }

    fun setScale(scale: Float) {
        sharedPreferences.putFloat("scale", scale)
    }


}