package com.devlomi.shared.ui.bookmark

sealed class BookmarkNavigationEvents {
    data class ToQuranPageWithPageNumber(val pageNumber:Int): BookmarkNavigationEvents()
}
