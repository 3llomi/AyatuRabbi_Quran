package com.devlomi.shared

import androidx.lifecycle.ViewModel
import com.devlomi.shared.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val settingsRepository: SettingsRepository) :
    ViewModel() {

    private val _preventScreenlock = MutableStateFlow<Boolean>(false)
    val preventScreenlock: StateFlow<Boolean> get() = _preventScreenlock

    init {
        _preventScreenlock.value = settingsRepository.preventScreenlock()
    }

    fun onSwitchChange(isEnabled: Boolean) {
        settingsRepository.setPreventScreenlock(isEnabled)
    }

}