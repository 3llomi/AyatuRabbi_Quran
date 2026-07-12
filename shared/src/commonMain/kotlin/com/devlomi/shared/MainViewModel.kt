package com.devlomi.shared

import androidx.lifecycle.ViewModel
import com.devlomi.shared.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    private val _keepScreenOn = MutableStateFlow<Boolean>(false)
    val keepScreenOn: StateFlow<Boolean> get() = _keepScreenOn

    private val _hideUI = MutableStateFlow<Unit>(Unit)
    val hideUI: StateFlow<Unit> get() = _hideUI


    fun loadKeepScreenOn() {
        _keepScreenOn.value = settingsRepository.preventScreenlock()
    }

    fun hasDownloadedFiles() = settingsRepository.hasDownloadedFiles()

    fun saveDeviceWidth(deviceWidth: Int) {
        settingsRepository.saveDeviceWidth(properSizeCalc.getProperWidth(deviceWidth))
    }

    //TODO HANDLE
    fun hideUI() {
        _hideUI.value = Unit
    }



}