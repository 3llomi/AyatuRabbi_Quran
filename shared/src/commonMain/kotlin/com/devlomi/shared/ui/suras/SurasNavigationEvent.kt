package com.devlomi.shared.ui.suras

sealed class SurasNavigationEvent {
    data class ToQuranPageWithPageNumber(val pageNumber: Int) : SurasNavigationEvent()
}
