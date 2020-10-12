package com.devlomi.ayaturabbi.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun deviceWidth(): Int {
        return sharedPreferences.getInt("width", 1260)
    }

    fun hasDownloadedFiles(): Boolean = sharedPreferences.getBoolean("files_downloaded", false)
    fun saveDeviceWidth(properWidth: Int) {
        if (sharedPreferences.contains("width")) return
        sharedPreferences.edit {
            putInt("width", properWidth)
        }
    }

    fun setDownloadFinished(b: Boolean) {
        sharedPreferences.edit {
            putBoolean("files_downloaded", b)
        }
    }
}