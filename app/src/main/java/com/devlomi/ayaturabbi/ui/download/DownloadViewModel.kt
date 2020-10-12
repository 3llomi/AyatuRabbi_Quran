package com.devlomi.ayaturabbi.ui.download

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlomi.ayaturabbi.network.DownloadingResource
import com.devlomi.ayaturabbi.settings.SettingsRepository

class DownloadViewModel @ViewModelInject constructor(private val settingsRepository: SettingsRepository) :
    ViewModel() {
    private var isDownloading = false
    private var downloadFinished = false
    private var currentState = DownloadingResource.None

    private val _startDownloadLiveData = MutableLiveData<Unit>()
    val startDownloadLiveData: LiveData<Unit> get() = _startDownloadLiveData

    private val _stopDownloadingLiveData = MutableLiveData<Unit>()
    val stopDownloadingLiveData: LiveData<Unit> get() = _stopDownloadingLiveData


    fun startDownloading() {
        if (isDownloading || downloadFinished) return

        _startDownloadLiveData.value = Unit

        isDownloading = true
    }

    fun stopDownloading() {
        isDownloading = false
        _stopDownloadingLiveData.value = Unit
    }

    fun downloadFinished() {
        downloadFinished = true
        settingsRepository.setDownloadFinished(true)
    }
}