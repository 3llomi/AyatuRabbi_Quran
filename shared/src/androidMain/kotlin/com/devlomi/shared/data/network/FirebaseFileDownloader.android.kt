package com.devlomi.shared.data.network



actual class FirebaseFileDownloader {
    actual suspend fun downlaodFile(
        storageRefPath: String,
        filePath: String,
        onProgress: (progress: Int) -> Unit
    ): Result<String> {
        TODO("Not yet implemented")
    }
}