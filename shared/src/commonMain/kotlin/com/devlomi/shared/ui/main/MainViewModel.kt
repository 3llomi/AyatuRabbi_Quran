package com.devlomi.shared.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.domain.ProperSizeCalc
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state


    init {
        viewModelScope.launch {
            val keepScreenOn = settingsRepository.preventScreenlock()
            val hasDownloadedFiles = settingsRepository.hasDownloadedFiles()
            _state.value = MainState(keepScreenOn, hasDownloadedFiles)
        }
    }

}