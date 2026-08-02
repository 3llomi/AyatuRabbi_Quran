package com.devlomi.shared.ui.search

import com.devlomi.shared.domain.model.SearchResult

data class SearchState(
    val searchResults: List<SearchResult> = listOf(),
    val query: String = "",
) {

}