package com.devlomi.shared.ui.download

import com.devlomi.shared.ui.suras.DialogActions

sealed class DownloadEvents {
    data object OnStartDownload : DownloadEvents()
    data object OnCancel : DownloadEvents()
    data class CancelDownloadAction(val action: DialogActions) : DownloadEvents()
    data class StartDownloadAction(val action: DialogActions) : DownloadEvents()
}