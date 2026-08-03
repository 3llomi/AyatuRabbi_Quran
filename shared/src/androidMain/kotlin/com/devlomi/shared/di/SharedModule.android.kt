package com.devlomi.shared.di

import com.devlomi.shared.CommonPreferences
import com.devlomi.shared.common.DirConstants
import com.devlomi.shared.data.network.FirebaseFileDownloader
import com.devlomi.shared.data.quran_datasource.QuranImagePathProvider
import com.devlomi.shared.ui.CommonDownloadService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(androidDBModule(),androidAppModule())

    single<CommonPreferences> {
        CommonPreferences(get())
    }

    single<QuranImagePathProvider> {
        QuranImagePathProvider(androidContext())
    }
    factory<CommonDownloadService> {
        CommonDownloadService(androidContext())
    }
    single<FirebaseFileDownloader> {
        FirebaseFileDownloader()
    }
    single<DirConstants>{
        DirConstants(androidContext())
    }
//    single<FileUnzipper> {
//        FileUnzipper()
//    }
}
