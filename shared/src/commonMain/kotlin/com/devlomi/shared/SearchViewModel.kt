package com.devlomi.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.db.search.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel
    (private val searchRepository: SearchRepository) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<SearchResult>>(emptyList())
    val searchResults: StateFlow<List<SearchResult>> get() = _searchResults
    private var job: Job? = null
    fun searchForAyah(query: String) {

        //cancel old job if exists
        job?.cancel()

        if (query.trim().isEmpty()) {
            _searchResults.value = mutableListOf()
            return
        }

        job = viewModelScope.launch(Dispatchers.IO) {
            val searchResult = searchRepository.searchForAyah(query)
            withContext(Main) {
                _searchResults.value = searchResult.toMutableList()
            }
        }

    }
}