package com.devlomi.shared

import androidx.lifecycle.viewModelScope
import com.devlomi.shared.settings.SettingsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    private val navigationChannel = Channel<DownloadNavigationEvent>()
    @NativeCoroutines
    val navigationEvent: Flow<DownloadNavigationEvent> = navigationChannel.receiveAsFlow()


    private val startDownloadChannel = Channel<Int>()
    @NativeCoroutines
    val startDownloadEvent: Flow<Int> = startDownloadChannel.receiveAsFlow()


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