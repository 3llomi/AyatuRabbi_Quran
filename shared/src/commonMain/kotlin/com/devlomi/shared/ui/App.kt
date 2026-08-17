package com.devlomi.shared.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.share_app_text
import com.devlomi.shared.common.asComposeColor
import com.devlomi.shared.common.getAppLink
import com.devlomi.shared.ui.bookmark.BookmarkNavigationEvents
import com.devlomi.shared.ui.bookmark.BookmarksScreen
import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.ui.components.ObserveAsEvent
import com.devlomi.shared.ui.download.DownloadNavigationEvent
import com.devlomi.shared.ui.download.DownloadScreen
import com.devlomi.shared.ui.download.DownloadViewModel
import com.devlomi.shared.ui.main.MainViewModel
import com.devlomi.shared.ui.quran_page.QuranPageEvents
import com.devlomi.shared.ui.quran_page.QuranPageNavigationEvent
import com.devlomi.shared.ui.quran_page.QuranPageScreen
import com.devlomi.shared.ui.quran_page.QuranPageViewModel
import com.devlomi.shared.ui.search.SearchNavigationEvents
import com.devlomi.shared.ui.search.SearchScreen
import com.devlomi.shared.ui.search.SearchViewModel
import com.devlomi.shared.ui.settings.SettingsNavigationEvents
import com.devlomi.shared.ui.settings.SettingsScreen
import com.devlomi.shared.ui.settings.SettingsViewModel
import com.devlomi.shared.ui.suras.SurasNavigationEvent
import com.devlomi.shared.ui.suras.SurasScreen
import com.devlomi.shared.ui.suras.SurasViewModel
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.getString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    hideSystemUi: (Boolean) -> Unit,
    onShareText: (String) -> Unit = {},
    onShareImage: (String) -> Unit = {},
    onShareApp: (text: String) -> Unit = {},
    exitApp: () -> Unit = {}
) {
    val sharedViewModel = koinViewModel<MainViewModel>()
    val sharedState = sharedViewModel.state.collectAsStateWithLifecycle().value
    val navController = rememberNavController()
    val initialScreen =
        if (sharedState.hasDownloadedFiles) Screen.QuranPage.createRoute(-1) else Screen.Download.route
    AppTheme {
        val defaultBackgroundColor = MaterialTheme.colorScheme.background
        var backgroundColor by remember{ mutableStateOf(defaultBackgroundColor) }
        var currentRoute: String? by remember { mutableStateOf(null) }

        Box(
            modifier = Modifier.background(backgroundColor)
                .systemBarsPadding()

        ) {
            SetWindowFlag(sharedState.keepScreenOn)
            //hide system bars if the user presses the recent button or minimized the app
            ObserveWindowFocusChange {
                val currentRoute = navController.currentDestination?.route
                if (currentRoute?.startsWith(Screen.QuranPage.route) != true) {
                    return@ObserveWindowFocusChange
                }
                hideSystemUi(it)
            }


            DisposableEffect(navController) {
                val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
                    val route = destination.route
                    currentRoute = route
                    hideSystemUi(route?.startsWith(Screen.QuranPage.route) == true)
                }
                navController.addOnDestinationChangedListener(listener)

                onDispose {
                    navController.removeOnDestinationChangedListener(listener)
                }
            }
            NavHost(
                navController = navController,
                startDestination = initialScreen,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(250))
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(250))
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(250))
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(250))
                }
            ) {
                composable(Screen.Download.route) {
                    val viewModel = koinViewModel<DownloadViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvent) {
                        when (it) {
                            DownloadNavigationEvent.ToQuranPage -> {
                                navController.navigate(Screen.QuranPage.createRoute(-1)) {
                                    popUpTo(Screen.Download.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                    DownloadScreen(state, onEvent = viewModel::onEvent)
                }
                composable(
                    Screen.QuranPage.routeWithArgs,
                    arguments = listOf(
                        navArgument(Screen.QuranPage.PAGE_NUMBER_ARG) {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    ),

                    ) { backStackEntry ->
                    val viewModel = koinViewModel<QuranPageViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    val pageNumberResult by backStackEntry.savedStateHandle
                        .getStateFlow<Int?>(Screen.QuranPage.PAGE_NUMBER_ARG, null)
                        .collectAsStateWithLifecycle()

                    LaunchedEffect(pageNumberResult) {
                        pageNumberResult?.let { pageNumber ->
                            if (pageNumber != -1) {
                                viewModel.onEvent(QuranPageEvents.OnPageChanged(pageNumber - 1))
                                // 4. Clear it so it doesn't re-trigger on configuration changes
                                backStackEntry.savedStateHandle.set<Int?>(
                                    Screen.QuranPage.PAGE_NUMBER_ARG,
                                    null
                                )
                            }
                        }
                    }

                    LaunchedEffect(state.backgroundColor,currentRoute){
                        backgroundColor = if (currentRoute?.startsWith( Screen.QuranPage.routeWithArgs) == true) {
                            state.backgroundColor.asComposeColor()
                        }else{
                            defaultBackgroundColor
                        }
                    }


                    ObserveAsEvent(viewModel.navigationEvent) {
                        when (it) {
                            QuranPageNavigationEvent.ToSuras -> {
                                navController.navigate(Screen.Suras.route)
                            }

                            QuranPageNavigationEvent.ToBookmarks -> {
                                navController.navigate(Screen.Bookmarks.route)
                            }

                            QuranPageNavigationEvent.ToSearch -> {
                                navController.navigate(Screen.Search.route)
                            }

                            QuranPageNavigationEvent.ToSettings -> {
                                navController.navigate(Screen.Settings.route)
                            }

                            is QuranPageNavigationEvent.ShareImage -> {
                                onShareImage(it.imagePath)
                            }

                            is QuranPageNavigationEvent.ShareText -> {
                                onShareText(it.text)
                            }

                            QuranPageNavigationEvent.BackPressed -> {
                                exitApp()
                            }
                        }
                    }
                    QuranPageScreen(
                        state = state,
                        onEvent = viewModel::onEvent,
                    )
                }
                composable(Screen.Suras.route) {
                    val viewModel = koinViewModel<SurasViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvent) {
                        when (it) {
                            is SurasNavigationEvent.ToQuranPageWithPageNumber -> {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    Screen.QuranPage.PAGE_NUMBER_ARG,
                                    it.pageNumber
                                )
                                navController.popBackStack()
                            }

                            SurasNavigationEvent.Back -> {
                                navController.popBackStack()
                            }
                        }
                    }
                    SurasScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Search.route) {
                    val viewModel = koinViewModel<SearchViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvents) {
                        when (it) {
                            is SearchNavigationEvents.BackToQuranPageWithPageNumber -> {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    Screen.QuranPage.PAGE_NUMBER_ARG,
                                    it.pageNumber
                                )
                                navController.popBackStack()
                            }
                            SearchNavigationEvents.Back -> {
                                navController.popBackStack()
                            }
                        }
                    }
                    SearchScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Bookmarks.route) {
                    val viewModel = koinViewModel<BookmarksViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvents) {
                        when (it) {
                            is BookmarkNavigationEvents.ToQuranPageWithPageNumber -> {
                                navController.previousBackStackEntry?.savedStateHandle?.set(
                                    Screen.QuranPage.PAGE_NUMBER_ARG,
                                    it.pageNumber
                                )
                                navController.popBackStack()
                            }
                            BookmarkNavigationEvents.Back -> {
                                navController.popBackStack()
                            }
                        }
                    }
                    BookmarksScreen(state, onEvent = viewModel::onEvent)
                }
                composable(Screen.Settings.route) {
                    val viewModel = koinViewModel<SettingsViewModel>()
                    val state = viewModel.state.collectAsStateWithLifecycle().value
                    ObserveAsEvent(viewModel.navigationEvents) {
                        when (it) {
                            SettingsNavigationEvents.Back -> {
                                navController.popBackStack()
                            }
                            SettingsNavigationEvents.ShareApp -> {
                                val text = runBlocking {
                                    val appLink = getAppLink()
                                    getString(Res.string.share_app_text, appLink)
                                }
                                onShareApp(text)
                            }
                        }
                    }
                    SettingsScreen(state, onEvent = viewModel::onEvent)
                }
            }
        }
    }
}