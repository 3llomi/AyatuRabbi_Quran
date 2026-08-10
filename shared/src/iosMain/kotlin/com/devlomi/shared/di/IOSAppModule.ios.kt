package com.devlomi.shared.di

import com.devlomi.shared.common.DirConstants
import com.devlomi.shared.data.network.FirebaseFileDownloader
import com.devlomi.shared.ui.CommonDownloadService
import org.koin.dsl.module


fun iosAppModule() = module {

    single<FirebaseFileDownloader> {
        FirebaseFileDownloader()
    }
    single<CommonDownloadService> {
        CommonDownloadService(get(),get())
    }
    single<DirConstants> {
        DirConstants()
    }

}