package com.devlomi.shared.di

import com.devlomi.shared.CommonPreferences
import com.devlomi.shared.quran_datasource.QuranImagePathProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(iosDBModule(),iosAppModule(),iosViewModelModule())

    single<CommonPreferences> {
        CommonPreferences()
    }

    single<QuranImagePathProvider> {
        QuranImagePathProvider()
    }
//    single<FileUnzipper> {
//        FileUnzipper()
//    }
}
