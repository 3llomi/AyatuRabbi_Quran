package com.devlomi.shared

import java.io.File

actual class FileUnzipper {
    actual fun unzip(zipFilePath: String, destDirectory: String) {
        val file = File(destDirectory)
        if (file.exists()) {
            file.mkdirs()
        }
        File(zipFilePath).unzip(File(destDirectory))

    }
}