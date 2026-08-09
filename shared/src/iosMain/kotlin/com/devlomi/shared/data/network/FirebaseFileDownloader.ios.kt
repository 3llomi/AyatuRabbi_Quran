package com.devlomi.shared.data.network

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import swiftPMImport.AyatuRabbi.shared.FIRStorage
import swiftPMImport.AyatuRabbi.shared.FIRStorageDownloadTask


actual class FirebaseFileDownloader {

    private var task: FIRStorageDownloadTask? = null
    actual suspend fun downlaodFile(
        storageRefPath: String,
        filePath: String,
        onProgress: (progress: Int) -> Unit
    ): String {
        val storage = FIRStorage.storage()
        val storageRef = storage.reference().child(storageRefPath)
        val localFile = NSURL.fileURLWithPath(filePath)
        return suspendCancellableCoroutine { continuation ->
            task = storageRef.writeToFile(localFile) { url, error ->
                if (error != null) {
                    continuation.resumeWith(Result.failure(Exception(error.localizedDescription)))
                } else {
                    continuation.resumeWith(Result.success(filePath))
                }
            }
        }
    }

    actual suspend fun cancel() {
        task?.cancel()
    }
}