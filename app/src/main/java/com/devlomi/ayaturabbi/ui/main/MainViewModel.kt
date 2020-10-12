package com.devlomi.ayaturabbi.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.ayaturabbi.settings.SettingsRepository
import com.devlomi.ayaturabbi.util.ProperSizeCalc
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    fun hasDownloadedFiles() = settingsRepository.hasDownloadedFiles()

    fun saveDeviceWidth(deviceWidth: Int) {
        settingsRepository.saveDeviceWidth(properSizeCalc.getProperWidth(deviceWidth))
    }

}
