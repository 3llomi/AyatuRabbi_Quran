package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.devlomi.ayaturabbi.ColorItem
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.settings.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class QuranPageViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val quranPageDataSource: QuranPageDataSource,
) : ViewModel() {

    private val _updateListLiveData = MutableLiveData<MutableList<QuranPageItem>>()
    val updateListLiveData: LiveData<MutableList<QuranPageItem>> get() = _updateListLiveData


    private val _updateBackgroundColor = MutableLiveData<Int>()
    val updateBackgroundColor: LiveData<Int> get() = _updateBackgroundColor

    private val _useWhiteColorLiveData = MutableLiveData<Boolean>()
    val useWhiteColorLiveData: LiveData<Boolean> get() = _useWhiteColorLiveData

    private val _setCurrentIndexLiveData = MutableLiveData<Int>()
    val setCurrentIndexLiveData: LiveData<Int> get() = _setCurrentIndexLiveData


    private var currentIndex = settingsRepository.getCurrentIndex()



    private val quranPageItems = mutableListOf<QuranPageItem>()

    private var backgroundColor =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())



    fun loadData() {
        val quranPageItemsDataSource =
            quranPageDataSource.getData()

        quranPageItems.addAll(quranPageItemsDataSource)
        _updateListLiveData.value = quranPageItems
        _setCurrentIndexLiveData.value = currentIndex
        updateBackgroundAndTextColors()
    }

    fun onPageChanged(newIndex: Int) {
        currentIndex = newIndex
//        val item =
//            quranPageDataSource.getQuranPageItemByIndex(newIndex + 1, quranImagesDirectory.path)
//
//        //TODO CHECK IF INDEX IS VALID
//        Log.d(
//            "3llomi",
//            "quran before chaning in list for newIndex $newIndex is ${quranPageItems[newIndex]}"
//        )
//
//        quranPageItems[newIndex] = item
//
//        Log.d("3llomi", "quran page in list for newIndex $newIndex is ${quranPageItems[newIndex]}")
//
//        _updateListLiveData.value = quranPageItems
//        _updateIndex.value = newIndex

    }

    fun colorPicked(colorItem: ColorItem) {
        backgroundColor = colorItem
        updateBackgroundAndTextColors()
    }

    private fun updateBackgroundAndTextColors() {
        val backgroundColorRes = getBackgroundColorResource()
        _useWhiteColorLiveData.value = useWhiteTextColor()
        _updateBackgroundColor.value = backgroundColorRes
    }

    private fun useWhiteTextColor() =
        backgroundColor == ColorItem.DKBLUE || backgroundColor == ColorItem.DKGRAY

    private fun getBackgroundColorResource() = when (backgroundColor) {
        ColorItem.DKGRAY -> R.color.dkgray
        ColorItem.BEIGE -> R.color.beige
        ColorItem.WHITE -> R.color.white
        else -> R.color.dkblue
    }

    fun onStop() {
        settingsRepository.saveBackgroundColor(backgroundColor.name)
        settingsRepository.saveCurrentIndex(currentIndex)
    }


}