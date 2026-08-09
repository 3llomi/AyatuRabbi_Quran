package com.devlomi.shared.common

expect class DirConstants {
    fun getDownloadTempPath(fileName: String): String
    fun getFilesPath(): String
    fun getQuranDataTempPath(): String
    fun getQuranImageBasePath(): String
}