package com.devlomi.shared.di

import com.devlomi.shared.CommonPreferences
import com.devlomi.shared.FileUnzipper
import com.devlomi.shared.ProperSizeCalc
import com.devlomi.shared.quran_datasource.QuranImagePathProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(androidDBModule(),androidAppModule(),androidViewModelModule())

    single<CommonPreferences> {
        CommonPreferences(get())
    }

    single<QuranImagePathProvider> {
        QuranImagePathProvider(androidContext())
    }
    single<FileUnzipper> {
        FileUnzipper()
    }
}
