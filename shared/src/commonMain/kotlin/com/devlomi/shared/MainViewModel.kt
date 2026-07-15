package com.devlomi.shared

import androidx.lifecycle.viewModelScope
import com.devlomi.shared.settings.SettingsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) : ViewModel() {

    private val _keepScreenOn = MutableStateFlow<Boolean>(false)
    @NativeCoroutinesState
    val keepScreenOn: StateFlow<Boolean> get() = _keepScreenOn

    private val _hideUIChannel = Channel<Unit>()
    @NativeCoroutines

    val hideUI: Flow<Unit> get() = _hideUIChannel.receiveAsFlow()


    fun loadKeepScreenOn() {
        _keepScreenOn.value = settingsRepository.preventScreenlock()
    }

    fun hasDownloadedFiles() = settingsRepository.hasDownloadedFiles()

    fun saveDeviceWidth(deviceWidth: Int) {
        settingsRepository.saveDeviceWidth(properSizeCalc.getProperWidth(deviceWidth))
    }

    //TODO HANDLE
    fun hideUI() {
        viewModelScope.launch {
            _hideUIChannel.send(Unit)
        }
    }


}