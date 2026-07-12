package com.devlomi.shared.db.ayahinfo

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devlomi.shared.db.DBFileNames
import com.devlomi.shared.db.quran_ar.QuranDB
import com.devlomi.shared.settings.SettingsRepository
import java.io.File

actual class DBFactory(private val context: Context, val settingsRepository: SettingsRepository) {
    actual fun createAyahInfoDB(): AyahInfoDB {
        val width = settingsRepository.deviceWidth()
        val file = File(context.filesDir, DBFileNames.ayahInfoNameDbPath(width))
        return Room.databaseBuilder(context, AyahInfoDB::class.java, AyahInfoDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    actual fun createQuranDB(): QuranDB {

        val file = File(context.filesDir, DBFileNames.quranDbPath)
        return Room.databaseBuilder(context, QuranDB::class.java, QuranDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    actual fun createBookmarkDB(): com.devlomi.shared.db.bookmark.BookmarkDB {
        return Room.databaseBuilder(context, com.devlomi.shared.db.bookmark.BookmarkDB::class.java, com.devlomi.shared.db.bookmark.BookmarkDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}