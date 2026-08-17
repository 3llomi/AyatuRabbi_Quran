package com.devlomi.shared.domain

import co.touchlab.kermit.Logger
import com.devlomi.shared.common.DirConstants
import com.devlomi.shared.data.db.DBFileNames
import com.devlomi.shared.unpackZip
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class ExtractAndCopyFiles(
    private val dirConstants: DirConstants,
) {
    suspend fun execute(width: Int, filePath: String) {
        val zipFile: okio.Path = filePath.toPath()
        val destDirPath: okio.Path = dirConstants.getQuranDataTempPath().toPath()
        FileSystem.SYSTEM.unpackZip(zipFile, destDirPath)
        copyFiles(width)
        deleteRecursively(PlatformFile(destDirPath.toString()))
        PlatformFile(filePath).delete(mustExist = false)
    }

    private suspend fun copyFiles(width: Int) {
        val temp = PlatformFile(dirConstants.getQuranDataTempPath())
        val filesDir = PlatformFile(dirConstants.getFilesPath())
        PlatformFile(filesDir, "db").createDirectories()
        PlatformFile(filesDir, "quran_images").createDirectories()
        filesDir.createDirectories(false)
        PlatformFile(temp, DBFileNames.ayahInfoNameDbPath(width)).copyTo(
            PlatformFile(
                filesDir,
                DBFileNames.ayahInfoNameDbPath(width)
            ),
        )


        PlatformFile(temp, DBFileNames.quranDbPath).copyTo(
            PlatformFile(
                filesDir,
                DBFileNames.quranDbPath
            )
        )


        copyDirRecursive(
            PlatformFile(temp, "width_$width"),
            PlatformFile(filesDir, "quran_images"),
        )


    }

    suspend fun deleteRecursively(file: PlatformFile): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (file.isDirectory()) {
                    // List all children and delete them first
                    val children = file.list()
                    for (child in children) {
                        val success = deleteRecursively(child)
                        if (!success) return@withContext false
                    }
                }
                // Delete the empty directory or individual file
                file.delete(mustExist = false)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun copyDirRecursive(src: PlatformFile, dst: PlatformFile) {
        if (!dst.exists()) {
            dst.createDirectories()
        }
        src.list().forEach { file ->
            val destFile = PlatformFile(dst, file.name)
            if (file.isDirectory()) {
                copyDirRecursive(file, destFile)
            } else {
                file.copyTo(destFile)
            }
        }

    }
}