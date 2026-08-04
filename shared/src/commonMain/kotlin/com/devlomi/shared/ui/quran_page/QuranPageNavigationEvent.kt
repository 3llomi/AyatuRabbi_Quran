package com.devlomi.shared.ui.quran_page

sealed class QuranPageNavigationEvent {
    data object ToSuras : QuranPageNavigationEvent()
    data object ToBookmarks : QuranPageNavigationEvent()
    data object ToSearch : QuranPageNavigationEvent()
    data object ToSettings : QuranPageNavigationEvent()
}

