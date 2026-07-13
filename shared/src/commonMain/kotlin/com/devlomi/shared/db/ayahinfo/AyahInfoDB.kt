package com.devlomi.shared.db.ayahinfo

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.devlomi.shared.db.bookmark.Bookmark

@Database(entities = [AyahInfo::class], version = 1, exportSchema = false)
@ConstructedBy(AyahInfoDBConstructor::class)
abstract class AyahInfoDB : RoomDatabase() {
 companion object{
     const val DB_NAME = "AyahInfo"
 }

    abstract fun ayahInfoDao(): AyahInfoDao
}

@Suppress("KotlinNoActualForExpect")
expect object AyahInfoDBConstructor : RoomDatabaseConstructor<AyahInfoDB> {
    override fun initialize(): AyahInfoDB
}



