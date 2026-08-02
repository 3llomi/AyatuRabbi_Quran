package com.devlomi.ayaturabbi

import android.app.Application
import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import com.devlomi.shared.di.androidViewModelModule
import com.devlomi.shared.di.initKoin
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext


class AyatuRabbiApp:Application(){
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AyatuRabbiApp)
        }
        runBlocking {
            val quranPageDataSource = getKoin().get<QuranPageDataSource>()
            quranPageDataSource.init()
        }
    }
}