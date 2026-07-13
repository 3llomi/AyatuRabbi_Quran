package com.devlomi.shared.db.quran_ar

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.devlomi.shared.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.db.quran_ar.entities.ArabicTextEntity
import com.devlomi.shared.db.quran_ar.entities.ShareTextEntity
import com.devlomi.shared.db.quran_ar.entities.VersesContentEntity


@Database(
    entities = [
        ArabicTextEntity::class, ShareTextEntity::class, VersesContentEntity::class
    ], version = 1, exportSchema = false
)
@ConstructedBy(QuranDBConstructor::class)
abstract class QuranDB : RoomDatabase() {
    companion object {
        const val DB_NAME = "QuranDB"
    }

    abstract fun quranDBDao(): QuranDBDao
}

@Suppress("KotlinNoActualForExpect")
expect object QuranDBConstructor : RoomDatabaseConstructor<QuranDB> {
    override fun initialize(): QuranDB
}
