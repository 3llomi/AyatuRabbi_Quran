package com.devlomi.shared.ui.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.data.network.DownloadRepository
import com.devlomi.shared.domain.ProperSizeCalc
import com.devlomi.shared.data.network.DownloadingResource
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.ui.DownloadService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DownloadViewModel(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc,
    private val downloadService: DownloadService,
    private val downloadRepository: DownloadRepository
) : ViewModel() {

    private val navigationChannel = Channel<DownloadNavigationEvent>()
    val navigationEvent: Flow<DownloadNavigationEvent> = navigationChannel.receiveAsFlow()

    private val _state = MutableStateFlow(DownloadScreenState())
    val state: StateFlow<DownloadScreenState>
        get() = _state.asStateFlow()


    init {
        viewModelScope.launch {
            downloadRepository.downloadResource.collect { resource ->
                if (resource is DownloadingResource.Success) {
                    launch {
                        settingsRepository.setDownloadFinished(true)
                        navigationChannel.send(DownloadNavigationEvent.ToQuranPage)
                    }
                }

                _state.update { it.copy(downlaodState = resource) }
            }
        }
    }


    fun onEvent(event: DownloadEvents) {
        when (event) {
            is DownloadEvents.OnStartDownload -> {
                _state.update { it.copy(showConfirmDownloadDialog = false) }
                val deviceWidth = settingsRepository.deviceWidth()
                val properWidth = properSizeCalc.getProperWidth(deviceWidth)
                downloadService.download(properWidth, "settingsRepository.getFilesDirPath()")//TODO
            }
            is DownloadEvents.OnCancel ->{
                downloadService.cancel()
            }
        }
    }
}