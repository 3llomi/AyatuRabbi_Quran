package com.devlomi.shared

sealed class DownloadNavigationEvent {
    object ToQuranPage: DownloadNavigationEvent()
}