package com.devlomi.shared

import com.devlomi.shared.settings.SettingsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val settingsRepository: SettingsRepository) :
    ViewModel() {

    private val _preventScreenlock = MutableStateFlow<Boolean>(false)
    @NativeCoroutinesState
    val preventScreenlock: StateFlow<Boolean> get() = _preventScreenlock

    init {
        _preventScreenlock.value = settingsRepository.preventScreenlock()
    }

    fun onSwitchChange(isEnabled: Boolean) {
        settingsRepository.setPreventScreenlock(isEnabled)
    }

}