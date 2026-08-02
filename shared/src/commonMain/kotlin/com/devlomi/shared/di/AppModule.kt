package com.devlomi.shared.di

import com.devlomi.shared.data.db.ayahinfo.AyahInfoRepository
import com.devlomi.shared.data.db.bookmark.BookmarkRepository
import com.devlomi.shared.data.db.quran_ar.QuranRepository
import com.devlomi.shared.data.db.search.SearchRepository
import com.devlomi.shared.domain.ProperSizeCalc
import com.devlomi.shared.data.quran_datasource.QuranImagesDataSource
import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import com.devlomi.shared.data.settings.SettingsRepository
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
        QuranPageDataSource(get())
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
        SearchRepository(get(), get())
    }


//    @Singleton
//    fun provideQuranImagesPathFile(@ApplicationContext context: Context) =
//        File(context.filesDir, "quran_images")


}