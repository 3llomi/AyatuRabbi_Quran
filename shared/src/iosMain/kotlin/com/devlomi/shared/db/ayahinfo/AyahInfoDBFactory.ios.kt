package com.devlomi.shared.db.ayahinfo

import androidx.room.Room
import androidx.sqlite.driver.NativeSQLiteDriver
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSLog
import platform.Foundation.NSUserDomainMask

actual class DBFactory(private val name: String) {
    //TODO DOUBLE CHECK
    actual fun createAyahInfoDB(): com.devlomi.shared.data.db.ayahinfo.AyahInfoDB {
        return Room.databaseBuilder<com.devlomi.shared.data.db.ayahinfo.AyahInfoDB>(
            name = (documentDirectory() + "/db/ayahinfo_1024.db").also {
                NSLog("Creating AyahInfoDB at path: $it")
            }
        )
            .setDriver(NativeSQLiteDriver()
            ).build()
    }

    actual fun createQuranDB(): com.devlomi.shared.data.db.quran_ar.QuranDB {
        return Room.databaseBuilder<com.devlomi.shared.data.db.quran_ar.QuranDB>(
            name = documentDirectory() + "/db/quran_db.db"
        )
            .setDriver(NativeSQLiteDriver())
            .build()
    }

    actual fun createBookmarkDB(): com.devlomi.shared.data.db.bookmark.BookmarkDB {
        return Room.databaseBuilder<com.devlomi.shared.data.db.bookmark.BookmarkDB>(
            name = documentDirectory() + "/${_root_ide_package_.com.devlomi.shared.data.db.bookmark.BookmarkDB.DB_NAME}.db"
        )
            .setDriver(NativeSQLiteDriver())
            .build()
    }

    //TODO
    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }

}