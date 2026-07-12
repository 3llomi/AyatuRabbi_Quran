package com.devlomi.shared

expect suspend fun getFile(
    path: String,
    filePath: String,
    onProgress: (progress: Double) -> Unit
): Result<String>
