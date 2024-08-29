package com.devlomi.ayaturabbi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.ayaturabbi.db.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor
    (private val searchRepository: SearchRepository) : ViewModel() {

    private val _searchResults = MutableLiveData<MutableList<SearchResult>>()
    val searchResults: LiveData<MutableList<SearchResult>> get() = _searchResults
    private var job: Job? = null
    fun searchForAyah(query: String) {

        //cancel old job if exists
        job?.cancel()

        if (query.trim().isEmpty()) {
            _searchResults.value = mutableListOf()
            return
        }

        job = viewModelScope.launch(IO) {
            val searchResult = searchRepository.searchForAyah(query)
            withContext(Main) {
                _searchResults.value = searchResult.toMutableList()
            }
        }

    }
}