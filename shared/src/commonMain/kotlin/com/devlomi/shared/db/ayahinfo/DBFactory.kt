package com.devlomi.shared.db.ayahinfo

import com.devlomi.shared.db.bookmark.BookmarkDB
import com.devlomi.shared.db.quran_ar.QuranDB

expect class DBFactory {
    fun createAyahInfoDB(): AyahInfoDB
    fun createQuranDB(): QuranDB
    fun createBookmarkDB(): BookmarkDB

}
