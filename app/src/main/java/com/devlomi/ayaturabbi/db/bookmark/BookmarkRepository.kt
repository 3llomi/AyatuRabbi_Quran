package com.devlomi.ayaturabbi.db.bookmark

import java.util.*
import javax.inject.Inject

class BookmarkRepository @Inject constructor(private val bookmarkDao: BookmarkDao) {

    suspend fun bookmark(pageNumber: Int, surahName: String, note: String? = null) {
        val bookmark = Bookmark(pageNumber, surahName, System.currentTimeMillis(), note)
        bookmarkDao.bookmark(bookmark)
    }

    suspend fun unBookmark(pageNumber: Int) {
        val bookmark = getBookmark(pageNumber)
        bookmark?.let { bookmark ->
            bookmarkDao.unBookmark(bookmark)
        }
    }


    private suspend fun getBookmark(pageNumber: Int) = bookmarkDao.getBookmarkByPage(pageNumber)

    suspend fun bookmarkExists(pageNumber: Int) = getBookmark(pageNumber) != null
}