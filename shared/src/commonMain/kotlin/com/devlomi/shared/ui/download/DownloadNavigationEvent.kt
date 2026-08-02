package com.devlomi.shared.ui.download


sealed class NavigationEvent(val route: String)
sealed class DownloadNavigationEvent(route: String): NavigationEvent(route) {
    data object ToQuranPage: DownloadNavigationEvent("quran")
}