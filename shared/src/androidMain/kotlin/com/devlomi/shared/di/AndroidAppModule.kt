package com.devlomi.shared.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File



fun androidAppModule() = module {
    single<File> {
        File(androidContext().filesDir, "quran_images")
    }
    single<SharedPreferences> {
        androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}