package com.devlomi.ayaturabbi.db.bookmark

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 1)
abstract class BookmarkDB : RoomDatabase() {
    companion object{
        const val DB_NAME = "bookmark"
    }
    abstract fun bookmarkDao(): BookmarkDao
}