package com.devlomi.ayaturabbi.network

sealed class DownloadingResource {
    object Success : DownloadingResource()
    data class Error(val e: Exception) : DownloadingResource()
    data class Loading(val progress: Int) : DownloadingResource()
    object None : DownloadingResource()
}