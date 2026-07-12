package com.devlomi.shared.db.bookmark

import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class BookmarkRepository (private val bookmarkDao: BookmarkDao) {

    @OptIn(ExperimentalTime::class)
    suspend fun bookmark(pageNumber: Int, surahName: String, note: String? = null) {
        val bookmark = Bookmark(
            pageNumber,
            surahName,
            Clock.System.now().toEpochMilliseconds(),
            note
        )
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