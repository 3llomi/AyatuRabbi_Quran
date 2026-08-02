package com.devlomi.shared.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel
    (private val searchRepository: com.devlomi.shared.data.db.search.SearchRepository) :
    ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState())
    val state: StateFlow<SearchState> get() = _state
    private var job: Job? = null
    fun onEvent(event: SearchEvents) {
        when (event) {
            is SearchEvents.OnSearchQueryChanged -> searchForAyah(event.query)
            is SearchEvents.OnSearchResultClicked -> {
                //TODO
            }
        }


    }

    private fun searchForAyah(query: String) {
        //cancel old job if exists
        job?.cancel()

        if (query.trim().isEmpty()) {
            _state.value = SearchState()
            return
        }

        job = viewModelScope.launch(Dispatchers.IO) {
            val searchResult = searchRepository.searchForAyah(query)
            withContext(Dispatchers.Main) {
                _state.update { it.copy(searchResults = searchResult.toMutableList()) }
            }
        }
    }
}