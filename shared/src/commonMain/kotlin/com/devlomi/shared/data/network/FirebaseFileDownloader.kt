package com.devlomi.shared.data.network

expect class FirebaseFileDownloader {
     suspend fun downlaodFile(storageRefPath: String, filePath: String, onProgress: (progress: Int) -> Unit): Result<String>
}