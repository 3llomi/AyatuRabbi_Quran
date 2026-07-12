package com.devlomi.shared

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.storage.ios
import dev.gitlive.firebase.storage.storage
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import platform.Foundation.NSURL
import platform.darwin.nil
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getFile(
    refPath: String,
    filePath: String
): Result<String> {
    val fileUrlPath = NSURL(fileURLWithPath = filePath)
        return withTimeoutOrNull(1000) {
            suspendCancellableCoroutine { cont ->
                Firebase.storage.ios.referenceWithPath(refPath).writeToFile(fileUrlPath) { data, error ->
                    if ((error != null && error != nil) || data == null)
                        cont.resume(Result.failure(Exception("File not found")))
                    else
                        cont.resume(Result.success(filePath))
                }
            }
        } ?: Result.failure(Exception("Timeout"))
}