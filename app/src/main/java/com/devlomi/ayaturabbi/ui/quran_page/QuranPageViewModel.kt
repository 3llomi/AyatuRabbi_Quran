package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File

class QuranPageViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val quranPageDataSource: QuranImagesDataSource
) : ViewModel() {

    private val _updateListLiveData = MutableLiveData<List<String>>()
    val updateListLiveData: LiveData<List<String>> get() = _updateListLiveData

    fun loadData() {
        val file = File(context.filesDir, "quran_images")
        val imagesPaths = quranPageDataSource.getQuranImagesPaths(file.path)
        _updateListLiveData.value = imagesPaths
    }
}