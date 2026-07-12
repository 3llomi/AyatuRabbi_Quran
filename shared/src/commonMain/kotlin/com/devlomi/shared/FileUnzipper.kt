package com.devlomi.shared

expect class FileUnzipper {
    fun unzip(zipFilePath: String, destDirectory: String)
}