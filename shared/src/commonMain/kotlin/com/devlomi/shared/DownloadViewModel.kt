package com.devlomi.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.settings.SettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    private val navigationChannel = Channel<DownloadNavigationEvent>()
    val navigationEvent = navigationChannel.receiveAsFlow()


    private val startDownloadChannel = Channel<Int>()
    val startDownloadEvent = startDownloadChannel.receiveAsFlow()


    private var properWidth = 0

    fun startDownloading() {
        viewModelScope.launch {
            startDownloadChannel.send(properWidth)
        }
    }


    fun downloadFinished() {
        settingsRepository.setDownloadFinished(true)
        viewModelScope.launch {
            navigationChannel.send(DownloadNavigationEvent.ToQuranPage)
        }
    }


    fun setDeviceWidth(deviceWidthPixels: Int) {
        properWidth = properSizeCalc.getProperWidth(deviceWidthPixels)
    }
}