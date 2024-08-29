package com.devlomi.ayaturabbi.ui.main


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlomi.ayaturabbi.settings.SettingsRepository
import com.devlomi.ayaturabbi.util.ProperSizeCalc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    private val _keepScreenOn = MutableLiveData<Boolean>()
    val keepScreenOn: LiveData<Boolean> get() = _keepScreenOn

    private val _hideUI = MutableLiveData<Unit>()
    val hideUI: LiveData<Unit> get() = _hideUI


    fun loadKeepScreenOn() {
        _keepScreenOn.value = settingsRepository.preventScreenlock()
    }

    fun hasDownloadedFiles() = settingsRepository.hasDownloadedFiles()

    fun saveDeviceWidth(deviceWidth: Int) {
        settingsRepository.saveDeviceWidth(properSizeCalc.getProperWidth(deviceWidth))
    }

    fun hideUI() {
        _hideUI.value = Unit
    }



}
