package com.devlomi.shared.data.db.bookmark

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor


@Database(entities = [Bookmark::class], version = 1)
@ConstructedBy(BookmarkDBConstructor::class)
abstract class BookmarkDB : RoomDatabase() {
    companion object{
        const val DB_NAME = "bookmark"
    }
    abstract fun bookmarkDao(): BookmarkDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object BookmarkDBConstructor : RoomDatabaseConstructor<BookmarkDB> {
    override fun initialize(): BookmarkDB
}