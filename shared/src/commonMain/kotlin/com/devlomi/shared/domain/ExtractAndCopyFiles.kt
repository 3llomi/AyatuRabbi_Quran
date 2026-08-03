package com.devlomi.shared.domain

import co.touchlab.kermit.Logger
import com.devlomi.shared.common.DirConstants
import com.devlomi.shared.data.db.DBFileNames
import com.devlomi.shared.unpackZip
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.copyTo
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.isDirectory
import io.github.vinceglb.filekit.list
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.path
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM

class ExtractAndCopyFiles(
    private val dirConstants: DirConstants,
) {
    suspend fun execute(width: Int, filePath: String) {
        val zipFile: okio.Path = filePath.toPath()
        val destDir: okio.Path = dirConstants.getQuranDataTempPath().toPath()
        Logger.d { "Unzipping file :${zipFile.name} - to ${destDir.name}" }
        FileSystem.SYSTEM.unpackZip(zipFile, destDir)
        Logger.d { "Unzipping Completed - attempting to copy files" }
        copyFiles(width)
        //TODO DELETE TEMP AND DATA.ZIP FILE
//        PlatformFile(filePath).copyTo(
//            PlatformFile(, "quran_data.zip"),
//        )

//        copyFiles(width)

    }

    private suspend fun copyFiles(width: Int) {

        val temp = PlatformFile(dirConstants.getQuranDataTempPath())
        val filesDir = PlatformFile(dirConstants.getFilesPath())
        PlatformFile(filesDir, "db").createDirectories()
        PlatformFile(filesDir, "quran_images").createDirectories()
        filesDir.createDirectories(false)
        Logger.d { "Copying ayah info name db path ${filesDir.path} - temp ${temp.path}" }
        PlatformFile(temp, DBFileNames.ayahInfoNameDbPath(width)).also {
            Logger.d { "SRC AYAH INFO ${it.path}" }
        }.copyTo(
            PlatformFile(
                filesDir,
                DBFileNames.ayahInfoNameDbPath(width)
            ).also {
                Logger.d { "Ayah INfo Path ${it.path}" }
            },
        )

        Logger.d { "Copying quranDbPath" }

        PlatformFile(temp, DBFileNames.quranDbPath).copyTo(
            PlatformFile(
                filesDir,
                DBFileNames.quranDbPath
            )
        )
        Logger.d { "Copying folder width $width" }

        //TODO RECURSIVE
        copyDirRecursive(
            PlatformFile(temp, "width_$width"),
            PlatformFile(filesDir, "quran_images"),
        )


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