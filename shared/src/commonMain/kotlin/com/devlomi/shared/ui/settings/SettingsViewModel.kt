package com.devlomi.shared.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.domain.appVer
import com.devlomi.shared.data.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) :
    ViewModel() {


    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> get() = _state.asStateFlow()

    private val navigationChannel = kotlinx.coroutines.channels.Channel<SettingsNavigationEvents>()
    val navigationEvents: kotlinx.coroutines.flow.Flow<SettingsNavigationEvents> =
        navigationChannel.receiveAsFlow()

    init {

        _state.update {
            it.copy(keepScreenOn = settingsRepository.preventScreenlock(), versionName = appVer())
        }
    }

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnSwitchChange -> {
                _state.update { it.copy(keepScreenOn = event.isEnabled) }
                settingsRepository.setPreventScreenlock(event.isEnabled)
            }

            is SettingsEvents.OnShareAppClick -> {
                viewModelScope.launch {
                    navigationChannel.send(SettingsNavigationEvents.ShareApp)
                }
            }
        }
    }

}