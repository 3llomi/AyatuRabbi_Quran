package com.devlomi.shared.di

import com.devlomi.shared.BookmarksViewModel
import com.devlomi.shared.DownloadViewModel
import com.devlomi.shared.MainViewModel
import com.devlomi.shared.QuranPageViewModel
import com.devlomi.shared.SearchViewModel
import com.devlomi.shared.SettingsViewModel
import com.devlomi.shared.SurasViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun androidViewModelModule() = module {
    viewModel<BookmarksViewModel> {
        BookmarksViewModel( get())
    }

    viewModel<DownloadViewModel> {
        DownloadViewModel(get(), get())
    }

    viewModel<MainViewModel> {
        MainViewModel(get(), get())
    }

    viewModel<QuranPageViewModel> {
        QuranPageViewModel( get(), get(), get(), get(), get())
    }

    viewModel<SearchViewModel> {
        SearchViewModel(get())
    }

    viewModel<SettingsViewModel>{
        SettingsViewModel(get())
    }
    viewModel<SurasViewModel> {
        SurasViewModel(get(),get())
    }

}