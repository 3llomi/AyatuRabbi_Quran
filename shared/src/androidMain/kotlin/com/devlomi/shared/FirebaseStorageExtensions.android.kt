package com.devlomi.shared

import android.util.Log
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.android
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume


actual suspend fun getFile(
    path: String,
    filePath: String,
    onProgress: (progress: Double) -> Unit
): Result<String> {
    Log.d("FirebaseStorageExtesnion","Downloading file from ref path: ${path} to local file ${filePath}")
    return suspendCancellableCoroutine { cont ->
        val task = Firebase.storage.android
            .getReference(path)
            .getFile(File(filePath))
        task
            .addOnCompleteListener {
                Log.d("FirebaseStorageExtensions", "Download completed: ${it.isSuccessful}")
                cont.resume(Result.success(filePath))
            }
            .addOnCanceledListener {
                Log.d("FirebaseStorageExtensions", "Download canceled")
                cont.resume(Result.failure(Exception("Download canceled")))
            }
            .addOnFailureListener {
                Log.d("FirebaseStorageExtensions", "Download failed: ${it.message}")
                cont.resume(Result.failure(Exception("Download canceled")))
            }
            .addOnProgressListener {
                val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount
                onProgress.invoke(progressDouble)
            }

        cont.invokeOnCancellation {
            task.cancel()
        }

    }

}