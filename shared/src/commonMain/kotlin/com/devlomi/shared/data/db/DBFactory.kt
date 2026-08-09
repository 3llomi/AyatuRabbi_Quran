package com.devlomi.shared.data.db

import com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB

expect class DBFactory {
    fun createAyahInfoDB(): AyahInfoDB
    fun createQuranDB(): QuranDB
    fun createBookmarkDB(): BookmarkDB

}