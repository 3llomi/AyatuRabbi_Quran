package com.devlomi.shared.ui

sealed class Screen(val route: String) {
    object Download : Screen("download")
    object QuranPage : Screen("quran_page")
    object Suras : Screen("suras")
    object Settings : Screen("settings")
    object Search : Screen("search")
    object Bookmarks : Screen("bookmarks")
}