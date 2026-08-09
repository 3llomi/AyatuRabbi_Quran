package com.devlomi.shared.di

import com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.data.db.DBFactory
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB
import org.koin.dsl.module


fun iosDBModule() = module {
    single<DBFactory>{
        DBFactory(get())
    }
    single<AyahInfoDB> {
        val dbFactory: DBFactory = get()
        dbFactory.createAyahInfoDB()
    }
    single<QuranDB> {
        val dbFactory: DBFactory = get()
        dbFactory.createQuranDB()
    }

    single<BookmarkDB> {
        val dbFactory: DBFactory = get()
        dbFactory.createBookmarkDB()
    }

}