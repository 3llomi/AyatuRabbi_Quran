package com.devlomi.shared.common

actual class DirConstants {
    actual fun getDownloadTempPath(fileName: String): String {
        val tmp = platform.Foundation.NSTemporaryDirectory()
        val base = if (tmp.endsWith("/")) tmp else "$tmp/"
        return base + fileName
    }

    actual fun getFilesPath(): String {
        val fm = platform.Foundation.NSFileManager.defaultManager
        val urls = fm.URLsForDirectory(platform.Foundation.NSDocumentDirectory, platform.Foundation.NSUserDomainMask)
        val first = (urls.firstOrNull() as? platform.Foundation.NSURL)?.path
        return first ?: fm.currentDirectoryPath
    }

    actual fun getQuranDataTempPath(): String {
        val tmp = platform.Foundation.NSTemporaryDirectory()
        val base = if (tmp.endsWith("/")) tmp else "$tmp/"
        val dir = base + "quran_data"
        platform.Foundation.NSFileManager.defaultManager.createDirectoryAtPath(dir, true, null, null)
        return dir
    }

    actual fun getQuranImageBasePath(): String {
        val fm = platform.Foundation.NSFileManager.defaultManager
        val urls = fm.URLsForDirectory(platform.Foundation.NSDocumentDirectory, platform.Foundation.NSUserDomainMask)
        val base = (urls.firstOrNull() as? platform.Foundation.NSURL)?.path ?: fm.currentDirectoryPath
        val dir = if (base.endsWith("/")) base + "quran_images" else "$base/quran_images"
        fm.createDirectoryAtPath(dir, true, null, null)
        return dir
    }
}