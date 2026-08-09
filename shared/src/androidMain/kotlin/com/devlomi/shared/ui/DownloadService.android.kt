package com.devlomi.shared.ui

import android.content.Context
import com.devlomi.shared.DownloadService

actual class CommonDownloadService(private val context: Context) {
    actual suspend fun download(width: Int, filePath: String) {
        DownloadService.start( width, filePath,context)
    }

    actual fun cancel() {
        DownloadService.stop(context)
    }
}