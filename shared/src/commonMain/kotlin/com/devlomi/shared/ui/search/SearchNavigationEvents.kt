package com.devlomi.shared.ui.search

sealed class SearchNavigationEvents {
    object Back : SearchNavigationEvents()
    data class BackToQuranPageWithPageNumber(val pageNumber: Int): SearchNavigationEvents()
}
