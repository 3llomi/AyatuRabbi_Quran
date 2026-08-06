package com.devlomi.shared.di

import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.ui.download.DownloadViewModel
import com.devlomi.shared.ui.main.MainViewModel
import com.devlomi.shared.ui.quran_page.QuranPageViewModel
import com.devlomi.shared.ui.search.SearchViewModel
import com.devlomi.shared.ui.settings.SettingsViewModel
import com.devlomi.shared.ui.suras.SurasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun viewModelModule() = module {
    viewModel<BookmarksViewModel> {
        BookmarksViewModel(get())
    }

    viewModel<DownloadViewModel> {
        DownloadViewModel(get(), get(), get(), get(), get())
    }

    viewModel<MainViewModel> {
        MainViewModel(get())
    }

    viewModel<QuranPageViewModel> {
        QuranPageViewModel(get(), get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(get())
    }
    viewModel<SurasViewModel> {
        SurasViewModel(get(), get())
    }

}