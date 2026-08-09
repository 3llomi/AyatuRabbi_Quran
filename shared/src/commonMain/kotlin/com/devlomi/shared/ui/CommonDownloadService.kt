package com.devlomi.shared.ui

expect class CommonDownloadService {
    suspend fun download(width: Int,filePath: String)
    fun cancel()
}