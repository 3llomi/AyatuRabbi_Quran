package com.devlomi.shared.ui.search

import com.devlomi.shared.domain.model.SearchResult

sealed class SearchEvents() {
    data class OnSearchQueryChanged(val query: String): SearchEvents()
    data class OnSearchResultClicked(val searchResult: SearchResult): SearchEvents()
}