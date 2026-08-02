package com.devlomi.shared.di

import org.koin.dsl.module


fun iosAppModule() = module {
    //TODO?
//    single<File> {
//        File(androidContext().filesDir, "quran_images")
//    }

    single<SharedString>{
        SharedString()
    }
}