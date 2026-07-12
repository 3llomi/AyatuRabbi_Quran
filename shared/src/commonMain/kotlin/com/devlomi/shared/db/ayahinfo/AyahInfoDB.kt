package com.devlomi.shared.db.ayahinfo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [AyahInfo::class], version = 1, exportSchema = false)

abstract class AyahInfoDB : RoomDatabase() {
 companion object{
     const val DB_NAME = "AyahInfo"
 }

    abstract fun ayahInfoDao(): AyahInfoDao
}

//@Suppress("KotlinNoActualForExpect")
//expect object AyahInfoDBConstructor : RoomDatabaseConstructor<AyahInfoDB> {
//    override fun initialize(): AyahInfoDB
//}



