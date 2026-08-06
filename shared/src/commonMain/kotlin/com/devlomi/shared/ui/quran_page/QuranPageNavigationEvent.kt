package com.devlomi.shared.ui.quran_page

sealed class QuranPageNavigationEvent {
    data object ToSuras : QuranPageNavigationEvent()
    data object ToBookmarks : QuranPageNavigationEvent()
    data object ToSearch : QuranPageNavigationEvent()
    data object ToSettings : QuranPageNavigationEvent()
    data class ShareText(val text: String ) : QuranPageNavigationEvent()
    data class ShareImage(val imagePath: String) : QuranPageNavigationEvent()
}

