package com.devlomi.shared.ui

expect class CommonDownloadService {
    fun download(width: Int,filePath: String)//TODO SHOULD WE MOVE THIS TO DIRCONSTANTS?
    fun cancel()
}