package com.devlomi.shared.ui.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devlomi.shared.common.DirConstants
import com.devlomi.shared.data.network.DownloadRepository
import com.devlomi.shared.domain.ProperSizeCalc
import com.devlomi.shared.data.network.DownloadingResource
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.ui.CommonDownloadService
import com.devlomi.shared.ui.suras.DialogActions
import com.devlomi.shared.ui.suras.DialogActionsWithQuery
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
    private val commonDownloadService: CommonDownloadService,
    private val downloadRepository: DownloadRepository,
    private val dirConstants: DirConstants
) : ViewModel() {

    private val navigationChannel = Channel<DownloadNavigationEvent>()
    val navigationEvent: Flow<DownloadNavigationEvent> = navigationChannel.receiveAsFlow()

    private val _state = MutableStateFlow(DownloadScreenState())
    val state: StateFlow<DownloadScreenState>
        get() = _state.asStateFlow()


    init {
        viewModelScope.launch {
            downloadRepository.downloadResource.collect { resource ->
                Logger.d { "DownloadViewModel ${resource.toString()}" }
                _state.update { it.copy(downlaodState = resource) }

                if (resource is DownloadingResource.Success) {
                    launch {
                        navigationChannel.send(DownloadNavigationEvent.ToQuranPage)
                    }
                }

            }
        }
    }


    fun onEvent(event: DownloadEvents) {
        when (event) {
            is DownloadEvents.OnStartDownload -> {
                _state.update { it.copy(showConfirmDownloadDialog = false) }
                startDownload()
            }

            is DownloadEvents.OnCancel -> {
                _state.update { it.copy(showConfirmCancelDownloadDialog = true) }
            }

            is DownloadEvents.CancelDownloadAction -> {
                when (event.action) {
                    is DialogActions.OnConfirm<*> -> {
                        _state.update { it.copy(showConfirmCancelDownloadDialog = false) }
                        Logger.d { "Cancelling Downlaod VM" }
                        commonDownloadService.cancel()
                    }

                    DialogActions.OnDismiss -> {
                        _state.update { it.copy(showConfirmCancelDownloadDialog = false) }
                    }

                }
            }

            is DownloadEvents.StartDownloadAction -> {
                when (event.action) {
                    is DialogActions.OnConfirm<*> -> {
                        _state.update { it.copy(showConfirmDownloadDialog = false) }
                        startDownload()
                    }

                    DialogActions.OnDismiss -> {
                        _state.update { it.copy(showConfirmDownloadDialog = false) }
                    }
                }
            }
        }
    }

    private fun startDownload() {
        val deviceWidth = settingsRepository.deviceWidth()
        val path = dirConstants.getDownloadTempPath("data.zip")
        viewModelScope.launch {
            commonDownloadService.download(deviceWidth, path)
        }
    }
}