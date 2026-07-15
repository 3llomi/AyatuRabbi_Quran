package com.devlomi.shared.di

import com.devlomi.shared.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.db.ayahinfo.DBFactory
import com.devlomi.shared.db.bookmark.BookmarkDB
import com.devlomi.shared.db.quran_ar.QuranDB
import org.koin.dsl.module


fun iosDBModule() = module {
    single<AyahInfoDB> {
        DBFactory("").createAyahInfoDB()
    }
    single<QuranDB> {
        DBFactory("").createQuranDB()
    }

    single<BookmarkDB> {
        DBFactory("").createBookmarkDB()
    }

}