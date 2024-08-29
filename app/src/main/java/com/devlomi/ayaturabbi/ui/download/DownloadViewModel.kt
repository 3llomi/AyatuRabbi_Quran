package com.devlomi.ayaturabbi.ui.download


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlomi.ayaturabbi.settings.SettingsRepository
import com.devlomi.ayaturabbi.util.ProperSizeCalc
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val properSizeCalc: ProperSizeCalc
) :
    ViewModel() {

    private val _startDownloadLiveData = MutableLiveData<Int>()
    val startDownloadLiveData: LiveData<Int> get() = _startDownloadLiveData

    private val _stopDownloadingLiveData = MutableLiveData<Unit>()
    val stopDownloadingLiveData: LiveData<Unit> get() = _stopDownloadingLiveData

    private var properWidth = 0

    fun startDownloading() {
        _startDownloadLiveData.value = properWidth
    }

    fun stopDownloading() {
        _stopDownloadingLiveData.value = Unit
    }

    fun downloadFinished() {
        settingsRepository.setDownloadFinished(true)
    }


    fun setDeviceWidth(deviceWidthPixels: Int) {
        properWidth = properSizeCalc.getProperWidth(deviceWidthPixels)
    }
}