package com.devlomi.shared

import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getFile(
    path: String,
    filePath: String,
    onProgress: (progress: Double) -> Unit
): Result<String> {
    return Result.success("")//TODO
//    val fileUrlPath = NSURL(fileURLWithPath = filePath)
//        return withTimeoutOrNull(1000) {
//            suspendCancellableCoroutine { cont ->
//                Firebase.storage.ios.referenceWithPath(refPath).writeToFile(fileUrlPath) { data, error ->
//                    if ((error != null && error != nil) || data == null)
//                        cont.resume(Result.failure(Exception("File not found")))
//                    else
//                        cont.resume(Result.success(filePath))
//                }
//            }
//        } ?: Result.failure(Exception("Timeout"))
}