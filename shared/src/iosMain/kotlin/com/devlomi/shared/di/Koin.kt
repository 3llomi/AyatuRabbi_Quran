package com.devlomi.shared.di

import com.devlomi.shared.BookmarksViewModel
import com.devlomi.shared.DownloadViewModel
import com.devlomi.shared.QuranPageViewModel
import com.devlomi.shared.SearchViewModel
import com.devlomi.shared.SharedString
import com.devlomi.shared.SurasViewModel
import com.devlomi.shared.network.DownloadRepository
import org.koin.mp.KoinPlatform

fun getSurasViewModel() : SurasViewModel = KoinPlatform.getKoin().get()
fun getBookmarksViewModel() : BookmarksViewModel = KoinPlatform.getKoin().get()
fun getSharedString() : SharedString = KoinPlatform.getKoin().get()
fun getSearchViewModel() : SearchViewModel = KoinPlatform.getKoin().get()
fun getQuranPageViewModel() : QuranPageViewModel = KoinPlatform.getKoin().get()
fun getDownloadRepository() : DownloadRepository = KoinPlatform.getKoin().get()
fun getDownloadViewModel() : DownloadViewModel = KoinPlatform.getKoin().get()

