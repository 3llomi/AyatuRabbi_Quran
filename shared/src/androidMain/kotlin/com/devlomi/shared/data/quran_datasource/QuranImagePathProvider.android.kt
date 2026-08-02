package com.devlomi.shared.data.quran_datasource

import android.content.Context
import java.io.File

actual class QuranImagePathProvider(private val context: Context) {
    actual fun getQuranImageBasePath(): String {
        return File(context.filesDir, "quran_images").path
    }
}