package com.devlomi.shared.ui

expect class CommonDownloadService {
    fun download(width: Int,filePath: String)
    fun cancel()
}