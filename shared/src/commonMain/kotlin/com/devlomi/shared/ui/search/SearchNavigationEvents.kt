package com.devlomi.shared.ui.search

sealed class SearchNavigationEvents {
    data class BackToQuranPageWithPageNumber(val pageNumber: Int): SearchNavigationEvents()
}
