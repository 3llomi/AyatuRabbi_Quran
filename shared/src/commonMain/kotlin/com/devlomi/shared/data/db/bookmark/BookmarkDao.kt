package com.devlomi.shared.data.db.bookmark

import androidx.room.*


@Dao
interface BookmarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookmark(bookmark: com.devlomi.shared.data.db.bookmark.Bookmark)

    @Delete
    suspend fun unBookmark(bookmark: com.devlomi.shared.data.db.bookmark.Bookmark)

    @Query("SELECT * FROM bookmark ORDER BY timestamp")
    suspend fun getAllBookmarks(): List<com.devlomi.shared.data.db.bookmark.Bookmark>

    @Query("SELECT * FROM bookmark WHERE pageNumber == :page LIMIT 1")
    suspend fun getBookmarkByPage(page: Int): com.devlomi.shared.data.db.bookmark.Bookmark?


}