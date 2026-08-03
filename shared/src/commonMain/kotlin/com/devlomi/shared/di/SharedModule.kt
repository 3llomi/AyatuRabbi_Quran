package com.devlomi.shared.di

import org.koin.core.module.Module
import org.koin.dsl.module

val sharedModule = module {
    includes(dbModule(),appModule(),viewModelModule(),platformModule)
}
expect val platformModule: Module

