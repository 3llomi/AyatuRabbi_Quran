package com.devlomi.shared.ui.download

sealed class DownloadEvents {
    data object OnStartDownload: DownloadEvents()
    data object OnCancel: DownloadEvents()
}