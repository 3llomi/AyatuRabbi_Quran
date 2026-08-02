package com.devlomi.shared.data.db.ayahinfo

import android.content.Context
import androidx.room.Room
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB
import com.devlomi.shared.data.settings.SettingsRepository
import java.io.File

actual class DBFactory(private val context: Context, val settingsRepository: SettingsRepository) {
    actual fun createAyahInfoDB(): AyahInfoDB {
        val width = settingsRepository.deviceWidth()
        val file = File(context.filesDir, com.devlomi.shared.data.db.DBFileNames.ayahInfoNameDbPath(width))
        return Room.databaseBuilder(context, AyahInfoDB::class.java, AyahInfoDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    actual fun createQuranDB(): QuranDB {

        val file = File(context.filesDir, com.devlomi.shared.data.db.DBFileNames.quranDbPath)
        return Room.databaseBuilder(context, QuranDB::class.java, QuranDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    actual fun createBookmarkDB(): BookmarkDB {
        return Room.databaseBuilder(context, BookmarkDB::class.java, BookmarkDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}