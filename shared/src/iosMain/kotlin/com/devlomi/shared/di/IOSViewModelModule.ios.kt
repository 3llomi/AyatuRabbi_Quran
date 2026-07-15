package com.devlomi.shared.di

import com.devlomi.shared.BookmarksViewModel
import com.devlomi.shared.DownloadViewModel
import com.devlomi.shared.MainViewModel
import com.devlomi.shared.QuranPageViewModel
import com.devlomi.shared.SearchViewModel
import com.devlomi.shared.SettingsViewModel
import com.devlomi.shared.SurasViewModel
import org.koin.dsl.module

fun iosViewModelModule() = module {
    factory<BookmarksViewModel> {
        BookmarksViewModel( get())
    }

    factory<DownloadViewModel> {
        DownloadViewModel(get(), get())
    }

    factory<MainViewModel> {
        MainViewModel(get(), get())
    }

    factory<QuranPageViewModel> {
        QuranPageViewModel( get(), get(), get(), get(), get())
    }

    factory<SearchViewModel> {
        SearchViewModel(get())
    }

    factory<SettingsViewModel>{
        SettingsViewModel(get())
    }
    factory<SurasViewModel> {
        SurasViewModel(get(),get())
    }

}