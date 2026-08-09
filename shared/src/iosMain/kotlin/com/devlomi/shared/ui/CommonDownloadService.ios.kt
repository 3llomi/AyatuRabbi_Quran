package com.devlomi.shared.ui

import co.touchlab.kermit.Logger
import com.devlomi.shared.data.network.DownloadRepository
import com.devlomi.shared.data.network.FirebaseFileDownloader
import platform.UIKit.UIApplication

actual class CommonDownloadService(
    private val firebaseFileDownloader: FirebaseFileDownloader,
    private val downloadRepository: DownloadRepository,
) {
    private var currentTaskId: ULong? = null
    actual suspend fun download(width: Int, filePath: String) {
        startService()
        try {
            val result = downloadRepository.download(width, filePath)
            if (result.isSuccess) {
                stopService()
            } else {
                Logger.e { "Downlaod Error - Download Service ${result.exceptionOrNull()?.message}" }
                throw result.exceptionOrNull() ?: Exception("Download Error")
            }

        } catch (e: Exception) {
            Logger.e { "Error Downloading - Downlaod Service ${e.message}" }
            stopService()
        }
    }

    private fun stopService() {
        currentTaskId?.let {
            UIApplication.sharedApplication.endBackgroundTask(it)
            currentTaskId = null
        }
    }
    private fun startService(){
        val taskId = UIApplication.sharedApplication.beginBackgroundTaskWithExpirationHandler {
            stopService()
        }
        currentTaskId = taskId
    }

    actual fun cancel() {
        firebaseFileDownloader.cancel()
    }
}