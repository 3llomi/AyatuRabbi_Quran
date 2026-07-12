package com.devlomi.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.quran_datasource.QuranPageDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SurasViewModel(
    private val sharedString: SharedString,
    private val quranPageDataSource: QuranPageDataSource,
) : ViewModel() {

    private lateinit var suras: List<Surah>
    private val _surasState = MutableStateFlow<List<Surah>>(listOf())
    val surasState: StateFlow<List<Surah>> get() = _surasState.asStateFlow()



    init {
        val surahNames = sharedString.getStringArray(StringArrays.SurahNames)
         suras = surahNames.mapIndexed { index, surahName ->
            Surah(surahName, index + 1)
        }

        _surasState.value = suras
    }

    //TODO DELETE IF NOT NEEDED
    fun loadData() {
        val surahNames = sharedString.getStringArray(StringArrays.SurahNames)
        val suras = surahNames.mapIndexed { index, surahName ->
            Surah(surahName, index + 1)
        }
        _surasState.value = suras
    }

    fun searchForSura(query: String) {
        if (query.trim().isEmpty()) {
            _surasState.value = suras
        } else {
            _surasState.value =
                suras.filter { it.surahName.contains(query) }.sortedBy { it.surahNumber }
        }
    }

    fun isPageNumberValid(page: String): Boolean {
        if (page.trim().isEmpty())
            return false

        if (page.isDigitsOnly()) {
            val allowedNumbers = (1..604)
            val pageNumber = page.toInt()
            if (allowedNumbers.contains(pageNumber)) {
                return true
            }
        }

        return false
    }

    fun getPageNumberByJuzoaIfValid(juzoa: String): Int? {
        if (juzoa.isDigitsOnly()) {
            val allowedNumbers = (1..30)
            val juzoaNumber = juzoa.toInt()
            if (allowedNumbers.contains(juzoaNumber)) {
                try {
                    //return page number if valid
                    return quranPageDataSource.getPageForJuzArray()[juzoaNumber - 1]
                } catch (e: Exception) {
                }
            }
        }
        return null

    }


}