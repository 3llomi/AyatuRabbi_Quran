package com.devlomi.shared.data.db.ayahinfo

import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB

expect class DBFactory {
    fun createAyahInfoDB(): com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
    fun createQuranDB(): com.devlomi.shared.data.db.quran_ar.QuranDB
    fun createBookmarkDB(): com.devlomi.shared.data.db.bookmark.BookmarkDB

}
