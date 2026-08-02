package com.devlomi.shared.di

import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.ui.download.DownloadViewModel
import com.devlomi.shared.ui.quran_page.QuranPageViewModel
import com.devlomi.shared.ui.search.SearchViewModel
import com.devlomi.shared.ui.suras.SurasViewModel
import org.koin.mp.KoinPlatform

fun initKoinIos() {
    initKoin()
}
fun getSurasViewModel() : SurasViewModel = KoinPlatform.getKoin().get()
fun getBookmarksViewModel() : BookmarksViewModel = KoinPlatform.getKoin().get()
fun getSharedString() : SharedString = KoinPlatform.getKoin().get()
fun getSearchViewModel() : SearchViewModel = KoinPlatform.getKoin().get()
fun getQuranPageViewModel() : QuranPageViewModel = KoinPlatform.getKoin().get()
fun getDownloadViewModel() : DownloadViewModel = KoinPlatform.getKoin().get()

