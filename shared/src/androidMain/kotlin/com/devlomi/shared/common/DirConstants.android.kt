package com.devlomi.shared.common

import android.content.Context
import java.io.File

actual class DirConstants(private val context: Context) {
    actual fun getDownloadTempPath(fileName: String): String {
        return File(context.cacheDir, fileName).absolutePath
    }

    actual fun getFilesPath(): String {
        return context.filesDir.path
    }

    actual fun getQuranDataTempPath(): String{
        return File(context.cacheDir, "quran_data").absolutePath
    }
}