package com.devlomi.ayaturabbi

import com.devlomi.shared.unzip
import java.io.File

object FileUnzipper {
    fun unZipFile(zipFilePath: String, targetLocation: String) {
        val file = File(targetLocation)
        if (file.exists()) {
            file.mkdirs()
        }
        File(zipFilePath).unzip(File(targetLocation))
    }//TODO DELETE?
}