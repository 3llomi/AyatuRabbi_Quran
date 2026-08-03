package com.devlomi.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.ui.bookmark.BookmarksScreen
import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.ui.components.ObserveAsEvent
import com.devlomi.shared.ui.download.DownloadNavigationEvent
import com.devlomi.shared.ui.download.DownloadScreen
import com.devlomi.shared.ui.download.DownloadViewModel
import com.devlomi.shared.ui.quran_page.QuranPageScreen
import com.devlomi.shared.ui.quran_page.QuranPageViewModel
import com.devlomi.shared.ui.search.SearchScreen
import com.devlomi.shared.ui.search.SearchViewModel
import com.devlomi.shared.ui.settings.SettingsScreen
import com.devlomi.shared.ui.settings.SettingsViewModel
import com.devlomi.shared.ui.suras.SurasScreen
import com.devlomi.shared.ui.suras.SurasViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val settingsRepository = koinInject<SettingsRepository>()
    val initialScreen =
        if (settingsRepository.hasDownloadedFiles()) Screen.QuranPage.route else Screen.Download.route
    Logger.d { "Initial Screen $initialScreen" }
    AppTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
                .safeDrawingPadding()
        ) {
            NavHost(
                navController = navController,
                startDestination = initialScreen,
            ) {
                composable(Screen.Download.route) {
                    val viewModel = koinViewModel<DownloadViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvent) {
                        when (it) {
                            DownloadNavigationEvent.ToQuranPage -> {
                                navController.navigate(Screen.QuranPage.route) {
                                    popUpTo(Screen.Download.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                    DownloadScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.QuranPage.route) {
                    val viewModel = koinViewModel<QuranPageViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
//                    QuranPageScreen(state, onEvent = viewModel::onEvent)
                    QuranPageScreen(
                        onOpenSuras = {},
                        onOpenSearch = {},
                        onOpenSettings = {},
                        onOpenBookmarks = {},
                        onShareText = {},
                        onShareImage = {}
                    )
                }
                composable(Screen.Suras.route) {
                    val viewModel = koinViewModel<SurasViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    SurasScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Search.route) {
                    val viewModel = koinViewModel<SearchViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    SearchScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Bookmarks.route) {
                    val viewModel = koinViewModel<BookmarksViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    BookmarksScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Settings.route) {
                    val viewModel = koinViewModel<SettingsViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    SettingsScreen(state, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}