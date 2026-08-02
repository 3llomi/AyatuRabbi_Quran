package com.devlomi.shared.ui.quran_page

import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.domain.ShareType

sealed class QuranPageEvents {
    data class OnPageChanged(val index: Int) : QuranPageEvents()
    data class OnColorPicked(val colorItem: ColorItem) : QuranPageEvents()
    data class OnShareTypeChosen(val shareType: ShareType) : QuranPageEvents()
    data class OnBookmarkWithNote(val note: String) : QuranPageEvents()
    data class OnSetPageScale(val thumbPosition: Int) : QuranPageEvents()
    data object OnBookmarkClicked : QuranPageEvents()
    data object OnShareDone : QuranPageEvents()
    data object OnZoomDone : QuranPageEvents()
    data object OnZoomClicked : QuranPageEvents()
    data object OnStop : QuranPageEvents()
}