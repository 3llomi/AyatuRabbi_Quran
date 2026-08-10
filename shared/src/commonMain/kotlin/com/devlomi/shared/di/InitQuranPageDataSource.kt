package com.devlomi.shared.di

import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import kotlinx.coroutines.runBlocking
import org.koin.mp.KoinPlatform.getKoin

fun initQuranPageDataSource() {
    runBlocking {
        val quranPageDataSource = getKoin().get<QuranPageDataSource>()
        quranPageDataSource.init()
    }
}