package com.devlomi.ayaturabbi.db.ayahinfo

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AyahInfo::class], version = 1, exportSchema = false)

abstract class AyahInfoDB : RoomDatabase() {
 companion object{
     const val DB_NAME = "AyahInfo"
 }

    abstract fun ayahInfoDao(): AyahInfoDao
}


