package com.devlomi.ayaturabbi.db.bookmark

import androidx.room.*


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmark(bookmark: Bookmark)

    @Delete
    suspend fun unBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM bookmark ORDER BY timestamp")
    suspend fun getAllBookmarks(): List<Bookmark>

    @Query("SELECT * FROM bookmark WHERE pageNumber == :page LIMIT 1")
    suspend fun getBookmarkByPage(page: Int): Bookmark?


}