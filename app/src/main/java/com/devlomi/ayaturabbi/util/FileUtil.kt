package com.devlomi.ayaturabbi.util

import java.io.File

object FileUtil {
    fun moveFilesFromDirectoryToAnother(srcFolder: String, destinationFolderPath: String) {
        File(srcFolder).copyRecursively(File(destinationFolderPath), true)
    }

    }