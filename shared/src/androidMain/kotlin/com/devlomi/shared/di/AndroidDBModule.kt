package com.devlomi.shared.di

import com.devlomi.shared.db.ayahinfo.AyahInfoDB
import com.devlomi.shared.db.ayahinfo.DBFactory
import com.devlomi.shared.db.bookmark.BookmarkDB
import com.devlomi.shared.db.quran_ar.QuranDB
import com.devlomi.shared.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun androidDBModule() = module {
    single<AyahInfoDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(context,settingsRepository).createAyahInfoDB()
    }
    single<QuranDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(context,settingsRepository).createQuranDB()
    }

    single<BookmarkDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(context,settingsRepository).createBookmarkDB()
    }

}