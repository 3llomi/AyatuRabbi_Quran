package com.devlomi.shared.di

import com.devlomi.shared.data.network.FirebaseFileDownloader
import org.koin.dsl.module


fun iosAppModule() = module {

    single<FirebaseFileDownloader> {
        FirebaseFileDownloader()
    }
}