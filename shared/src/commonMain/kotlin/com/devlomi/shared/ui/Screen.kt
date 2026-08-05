package com.devlomi.shared.ui

sealed class Screen(val route: String) {
    object Download : Screen("download")
    object QuranPage : Screen("quran_page") {
        const val PAGE_NUMBER_ARG = "pageNumber"
        val routeWithArgs: String = "$route/{$PAGE_NUMBER_ARG}"

        fun createRoute(pageNumber: Int): String = "$route/$pageNumber"
    }
    object Suras : Screen("suras")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object Bookmarks : Screen("bookmarks")
}