package com.devlomi.shared.di

import androidx.room.Room
import com.devlomi.shared.db.DBFileNames
import com.devlomi.shared.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.db.ayahinfo.AyahInfoDao
import com.devlomi.shared.db.bookmark.BookmarkDB
import com.devlomi.shared.db.bookmark.BookmarkDao
import com.devlomi.shared.db.quran_ar.QuranDB
import com.devlomi.shared.db.quran_ar.QuranDBDao
import org.koin.dsl.module

fun dbModule() = module {

    single<AyahInfoDao> {
        val ayahInfoDB: AyahInfoDB = get()
        ayahInfoDB.ayahInfoDao()
    }

    single<QuranDBDao> {
        val quranDb: QuranDB = get()
        quranDb.quranDBDao()
    }

    single<BookmarkDao> {
        val bookmarkDB: BookmarkDB = get()
        bookmarkDB.bookmarkDao()
    }
}
