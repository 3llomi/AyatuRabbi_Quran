package com.devlomi.ayaturabbi.extensions

import android.os.Build
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

//FileExt.kt

data class ZipIO (val entry: ZipEntry, val output: File)

fun File.unzip(unzipLocationRoot: File? = null) {

    val rootFolder = unzipLocationRoot ?: File(parentFile.absolutePath + File.separator + nameWithoutExtension)
    if (!rootFolder.exists()) {
        rootFolder.mkdirs()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        dalvik.system.ZipPathValidator.clearCallback()
    }
    ZipFile(this).use { zip ->
        zip
            .entries()
            .asSequence()
            .map {
                val outputFile = File(rootFolder.absolutePath + File.separator + it.name)
                ZipIO(it, outputFile)
            }
            .map {
                it.output.parentFile?.run{
                    if (!exists()) mkdirs()
                }
                it
            }
            .filter { !it.entry.isDirectory }
            .forEach { (entry, output) ->
                zip.getInputStream(entry).use { input ->
                    output.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
    }

}