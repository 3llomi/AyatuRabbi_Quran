package com.devlomi.shared.data.network

import co.touchlab.kermit.Logger
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File


actual class FirebaseFileDownloader {
    private var task: FileDownloadTask? = null
    actual suspend fun downlaodFile(
        storageRefPath: String,
        filePath: String,
        onProgress: (progress: Int) -> Unit
    ): String {
        val file = File(filePath)
        return suspendCancellableCoroutine { continuation ->
            task = FirebaseStorage.getInstance().getReference(storageRefPath).getFile(file)
            task?.addOnProgressListener {

                val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount
                Logger.d { "ProgressBytesTransferred ${it.bytesTransferred} - totalByte ${it.totalByteCount}" }
                //get progress
                Logger.d { "Progress $progressDouble" }
                val progress = progressDouble.toInt()
                onProgress(progress)
            }?.addOnCompleteListener {
                Logger.d { "addOnCompleteListener ${it.isSuccessful}" }
                if (it.isSuccessful) {
                    continuation.resumeWith(Result.success(filePath))
                } else {
                    it.exception?.let {
                        continuation.resumeWith(Result.failure(it))
                    } ?: run { continuation.resumeWith(Result.failure(Exception("Unknown Error"))) }
                }

            }
        }
    }

    actual suspend fun cancel() {
        task?.cancel()
    }
}