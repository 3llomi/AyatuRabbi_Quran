package com.devlomi.shared.di

import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.ui.download.DownloadViewModel
import com.devlomi.shared.MainViewModel
import com.devlomi.shared.ui.quran_page.QuranPageViewModel
import com.devlomi.shared.ui.search.SearchViewModel
import com.devlomi.shared.ui.settings.SettingsViewModel
import com.devlomi.shared.ui.suras.SurasViewModel
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