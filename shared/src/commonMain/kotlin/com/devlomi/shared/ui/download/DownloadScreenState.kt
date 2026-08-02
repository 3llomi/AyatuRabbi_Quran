package com.devlomi.shared.ui.download

import com.devlomi.shared.data.network.DownloadingResource

data class DownloadScreenState(
    val downlaodState: DownloadingResource = DownloadingResource.None,
    val showConfirmDownloadDialog: Boolean = true,
)