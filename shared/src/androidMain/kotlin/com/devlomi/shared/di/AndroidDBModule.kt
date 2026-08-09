package com.devlomi.shared.di

import com.devlomi.shared.data.db.DBFactory
import com.devlomi.shared.data.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun androidDBModule() = module {
    single<com.devlomi.shared.data.db.ayahinfo.AyahInfoDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(
            context,
            settingsRepository
        ).createAyahInfoDB()
    }
    single<com.devlomi.shared.data.db.quran_ar.QuranDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(
            context,
            settingsRepository
        ).createQuranDB()
    }

    single<com.devlomi.shared.data.db.bookmark.BookmarkDB> {
        val context = androidContext()
        val settingsRepository = get<SettingsRepository>()
        DBFactory(
            context,
            settingsRepository
        ).createBookmarkDB()
    }

}