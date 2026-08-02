package com.devlomi.shared.ui.settings

import androidx.lifecycle.ViewModel
import com.devlomi.shared.domain.appVer
import com.devlomi.shared.data.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(private val settingsRepository: SettingsRepository) :
    ViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> get() = _state.asStateFlow()

    init {

        _state.update {
            it.copy(keepScreenOn = settingsRepository.preventScreenlock(), versionName = appVer())
        }
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnSwitchChange -> {
                settingsRepository.setPreventScreenlock(event.isEnabled)
            }
        }
    }

}