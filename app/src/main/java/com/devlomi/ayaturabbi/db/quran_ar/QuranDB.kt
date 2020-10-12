package com.devlomi.ayaturabbi.db.quran_ar

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devlomi.ayaturabbi.db.quran_ar.entities.*


@Database(
    entities = [
        ArabicTextEntity::class, /*PropertiesEntity::class,*/ ShareTextEntity::class,
        VersesContentEntity::class, VersesSegDirEntity::class, VersesSegmentsEntity::class
    ], version = 1, exportSchema = false
)
abstract class QuranDB:RoomDatabase() {
    companion object {
        const val DB_NAME = "QuranDB"
    }

    abstract fun quranDBDao(): QuranDBDao
}