package com.devlomi.shared.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.NativeSQLiteDriver
import androidx.sqlite.execSQL
import co.touchlab.kermit.Logger
import com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB
import com.devlomi.shared.data.settings.SettingsRepository
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DBFactory(private val settingsRepository: SettingsRepository) {
    actual fun createAyahInfoDB(): AyahInfoDB {
        val deviceWidth = settingsRepository.deviceWidth()
        val dbFilePath = documentDirectory() + "/" + DBFileNames.ayahInfoNameDbPath(deviceWidth)
        return Room.databaseBuilder<AyahInfoDB>(
            name = dbFilePath,
        ).fallbackToDestructiveMigration(true)
            .setDriver(NativeSQLiteDriver())
            .build()
    }

    actual fun createQuranDB(): QuranDB {
        val dbFilePath = documentDirectory() + "/" + DBFileNames.quranDbPath

        return Room.databaseBuilder<QuranDB>(
            name = dbFilePath,
        ).fallbackToDestructiveMigration(true).setDriver(NativeSQLiteDriver())
            .build()
    }

    actual fun createBookmarkDB(): BookmarkDB {
        val dbFilePath = documentDirectory() + "/" + BookmarkDB.Companion.DB_NAME
        return Room.databaseBuilder<BookmarkDB>(
            name = dbFilePath,
        ).fallbackToDestructiveMigration(true)
            .setDriver(NativeSQLiteDriver())
            .build()
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.Companion.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}