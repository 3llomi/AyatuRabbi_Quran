package com.devlomi.shared.db.ayahinfo

import androidx.room.Room
import com.devlomi.shared.db.bookmark.BookmarkDB
import com.devlomi.shared.db.quran_ar.QuranDB
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DBFactory(private val name: String) {
    //TODO DOUBLE CHECK
    actual fun createAyahInfoDB(): AyahInfoDB{
        return Room.databaseBuilder<AyahInfoDB>(
            name = name
        ).build()
    }
    actual fun createQuranDB(): QuranDB{
        return Room.databaseBuilder<QuranDB>(
            name = name
        ).build()
    }

    actual fun createBookmarkDB(): BookmarkDB{
        return Room.databaseBuilder<BookmarkDB>(
            name = name
        ).build()
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