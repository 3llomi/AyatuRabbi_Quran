package com.devlomi.shared.di

import androidx.room.Room
import com.devlomi.shared.data.db.DBFileNames
import com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.data.db.ayahinfo.AyahInfoDao
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.bookmark.BookmarkDao
import com.devlomi.shared.data.db.quran_ar.QuranDB
import com.devlomi.shared.data.db.quran_ar.QuranDBDao
import org.koin.dsl.module

fun dbModule() = module {

    single<com.devlomi.shared.data.db.ayahinfo.AyahInfoDao> {
        val ayahInfoDB: com.devlomi.shared.data.db.ayahinfo.AyahInfoDB = get()
        ayahInfoDB.ayahInfoDao()
    }

    single<com.devlomi.shared.data.db.quran_ar.QuranDBDao> {
        val quranDb: com.devlomi.shared.data.db.quran_ar.QuranDB = get()
        quranDb.quranDBDao()
    }

    single<com.devlomi.shared.data.db.bookmark.BookmarkDao> {
        val bookmarkDB: com.devlomi.shared.data.db.bookmark.BookmarkDB = get()
        bookmarkDB.bookmarkDao()
    }
}
