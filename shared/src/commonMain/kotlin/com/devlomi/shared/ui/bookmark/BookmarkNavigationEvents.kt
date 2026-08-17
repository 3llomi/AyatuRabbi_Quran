package com.devlomi.shared.ui.bookmark

sealed class BookmarkNavigationEvents {
    object Back : BookmarkNavigationEvents()
    data class ToQuranPageWithPageNumber(val pageNumber:Int): BookmarkNavigationEvents()
}
