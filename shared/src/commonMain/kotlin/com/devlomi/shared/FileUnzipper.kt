package com.devlomi.shared
//
//import com.oldguy.common.io.File
//import com.oldguy.common.io.FileMode
//import com.oldguy.common.io.ZipFile
//
//
//
//suspend fun unzipFile(zipFilePath: String, outputDirectoryPath: String) {
//    val destDir = File(outputDirectoryPath)
//    if (!destDir.exists) {
//        destDir.makeDirectory()
//    }
//
//    val archiveFile = File(zipFilePath)
//    val zipFile = ZipFile(archiveFile, FileMode.Read)
//
//    zipFile.use { file ->
//        // kmp-io handles traversing entries and writing extracted files.
//        file.extractToDirectory(destDir)
//    }
//}
//TODO DELETE