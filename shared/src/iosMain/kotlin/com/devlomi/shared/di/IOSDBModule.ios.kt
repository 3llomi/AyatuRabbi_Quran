package com.devlomi.shared.di

import com.devlomi.shared.data.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.data.db.ayahinfo.DBFactory
import com.devlomi.shared.data.db.bookmark.BookmarkDB
import com.devlomi.shared.data.db.quran_ar.QuranDB
import org.koin.dsl.module


fun iosDBModule() = module {
    single<com.devlomi.shared.data.db.ayahinfo.AyahInfoDB> {
        _root_ide_package_.com.devlomi.shared.data.db.ayahinfo.DBFactory("").createAyahInfoDB()
    }
    single<com.devlomi.shared.data.db.quran_ar.QuranDB> {
        _root_ide_package_.com.devlomi.shared.data.db.ayahinfo.DBFactory("").createQuranDB()
    }

    single<com.devlomi.shared.data.db.bookmark.BookmarkDB> {
        _root_ide_package_.com.devlomi.shared.data.db.ayahinfo.DBFactory("").createBookmarkDB()
    }

}