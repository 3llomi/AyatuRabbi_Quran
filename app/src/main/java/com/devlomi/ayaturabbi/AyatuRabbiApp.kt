package com.devlomi.ayaturabbi

import android.app.Application
import com.devlomi.shared.di.androidViewModelModule
import com.devlomi.shared.di.initKoin
import org.koin.android.ext.koin.androidContext


class AyatuRabbiApp:Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AyatuRabbiApp)
        }
    }
}