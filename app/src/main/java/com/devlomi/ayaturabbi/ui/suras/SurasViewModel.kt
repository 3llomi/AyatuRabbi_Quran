package com.devlomi.ayaturabbi.ui.suras

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devlomi.ayaturabbi.R
import dagger.hilt.android.qualifiers.ApplicationContext

class SurasViewModel @ViewModelInject constructor(@ApplicationContext private val context: Context) :
    ViewModel() {

    private val _suras = MutableLiveData<List<Surah>>()
    val suras: LiveData<List<Surah>> get() = _suras

    private val _filterdSuras = MutableLiveData<List<Surah>>()
    val filterdSuras: LiveData<List<Surah>> get() = _filterdSuras


    fun loadData() {

        val surahNames = context.resources.getStringArray(R.array.surah_names).toList()
        val suras = surahNames.mapIndexed { index, surahName ->
            Surah(surahName, index + 1)
        }
        _suras.value = suras
    }

    fun searchForSura(query: String) {
        if (query.trim().isEmpty()) {
            _suras.value = _suras.value
        } else {
            _filterdSuras.value =
                _suras.value?.filter { it.surahName.contains(query) }?.sortedBy { it.surahNumber }
        }
    }

    fun isPageNumberValid(page: String): Boolean {
        if (page.trim().isEmpty())
            return false

        if (TextUtils.isDigitsOnly(page)) {
            val allowedNumbers = (1..604)
            Log.d("3llomi","allowedNumbers $allowedNumbers")
            val pageNumber = page.toInt()
            if (allowedNumbers.contains(pageNumber)) {
                return true
            }
        }

        return false
    }


}
