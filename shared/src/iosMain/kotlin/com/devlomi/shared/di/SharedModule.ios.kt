package com.devlomi.shared.di

import com.devlomi.shared.CommonPreferences
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    includes(iosDBModule(),iosAppModule())

    single<CommonPreferences> {
        CommonPreferences()
    }

}
