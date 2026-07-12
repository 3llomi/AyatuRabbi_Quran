package com.devlomi.shared.db.quran_ar

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devlomi.shared.db.quran_ar.entities.ArabicTextEntity
import com.devlomi.shared.db.quran_ar.entities.ShareTextEntity
import com.devlomi.shared.db.quran_ar.entities.VersesContentEntity


@Database(
    entities = [
        ArabicTextEntity::class, ShareTextEntity::class, VersesContentEntity::class
    ], version = 1, exportSchema = false
)
abstract class QuranDB : RoomDatabase() {
    companion object {
        const val DB_NAME = "QuranDB"
    }

    abstract fun quranDBDao(): QuranDBDao
}