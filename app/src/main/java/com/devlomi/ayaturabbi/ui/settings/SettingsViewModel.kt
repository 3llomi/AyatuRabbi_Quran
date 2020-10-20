package com.devlomi.ayaturabbi.ui.settings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlomi.ayaturabbi.settings.SettingsRepository

class SettingsViewModel @ViewModelInject constructor(private val settingsRepository: SettingsRepository) :
    ViewModel() {

    private val _preventScreenlock = MutableLiveData<Boolean>()
    val preventScreenlock: LiveData<Boolean> get() = _preventScreenlock

    init {
        _preventScreenlock.value = settingsRepository.preventScreenlock()
    }

    fun onSwitchChange(isEnabled: Boolean) {
        settingsRepository.setPreventScreenlock(isEnabled)
    }

}