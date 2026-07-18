package com.devlomi.shared.di

import com.devlomi.shared.CommonPreferences
import com.devlomi.shared.ProperSizeCalc
import com.devlomi.shared.db.ayahinfo.AyahInfoRepository
import com.devlomi.shared.db.bookmark.BookmarkRepository
import com.devlomi.shared.db.quran_ar.QuranRepository
import com.devlomi.shared.db.search.SearchRepository
import com.devlomi.shared.quran_datasource.QuranImagesDataSource
import com.devlomi.shared.quran_datasource.QuranPageDataSource
import com.devlomi.shared.settings.SettingsRepository
import org.koin.dsl.module


fun appModule() = module {
    single<SettingsRepository>{
        SettingsRepository(get())
    }
    single<ProperSizeCalc> {
        ProperSizeCalc()
    }

    single<QuranImagesDataSource>{
        QuranImagesDataSource(get())
    }
    single<QuranPageDataSource> {
        QuranPageDataSource(get(),get())
    }

    factory<QuranRepository> {
        QuranRepository(get(), get(), get())
    }

    factory<AyahInfoRepository> {
        AyahInfoRepository(get())
    }

    factory<BookmarkRepository> {
        BookmarkRepository(get())
    }

    factory<SearchRepository> {
        SearchRepository(get(), get(), get())
    }


//    @Singleton
//    fun provideQuranImagesPathFile(@ApplicationContext context: Context) =
//        File(context.filesDir, "quran_images")


}