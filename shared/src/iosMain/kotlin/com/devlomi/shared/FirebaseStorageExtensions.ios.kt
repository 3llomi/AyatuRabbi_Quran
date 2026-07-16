package com.devlomi.shared

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.ios
import dev.gitlive.firebase.storage.storage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.darwin.nil
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getFile(
    path: String,
    filePath: String,
    onProgress: (progress: Double) -> Unit
): Result<String> {
    return Result.success("")
//    val fileUrlPath = NSURL(fileURLWithPath = filePath)
//    return suspendCancellableCoroutine { cont ->
//        Firebase.storage.ios.referenceWithPath(path).writeToFile(fileUrlPath) { data, error ->
//            if ((error != null && error != nil) || data == null)
//                cont.resume(Result.failure(Exception("File not found")))
//            else
//                cont.resume(Result.success(filePath))
//        }
//    }
}