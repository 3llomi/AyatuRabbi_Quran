package com.devlomi.ayaturabbi.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import com.devlomi.ayaturabbi.ColorItem
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


    fun saveBackgroundColor(backgroundColorName: String) {
        sharedPreferences.edit {
            putString("bgColorName", backgroundColorName)
        }
    }

    fun getBackgroundColorName() =
        sharedPreferences.getString("bgColorName", ColorItem.DKGRAY.name)!!

    fun saveCurrentIndex(currentIndex: Int) {
        sharedPreferences.edit {
            putInt("current_index", currentIndex)
        }
    }

    fun getCurrentIndex() = sharedPreferences.getInt("current_index", 0)

    fun preventScreenlock() = sharedPreferences.getBoolean("prevent_screen_lock", true)
    fun setPreventScreenlock(boolean: Boolean) {
        sharedPreferences.edit {
            putBoolean("prevent_screen_lock", boolean)
        }
    }


}