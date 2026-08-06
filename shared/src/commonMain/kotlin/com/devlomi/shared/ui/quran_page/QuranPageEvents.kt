package com.devlomi.shared.ui.quran_page

import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.ui.suras.DialogActions
import com.devlomi.shared.ui.suras.DialogActionsWithQuery

sealed class QuranPageEvents {
    data class OnPageChanged(val index: Int) : QuranPageEvents()
    data class OnPageSwipe(val index: Int) : QuranPageEvents()
    data class OnColorPicked(val colorItem: ColorItem) : QuranPageEvents()
    data class OnPageSliderChange(val thumbPosition: Int) : QuranPageEvents()
    data object OnBookmarkClicked : QuranPageEvents()
    data object OnZoomDone : QuranPageEvents()
    data object OnZoomClicked : QuranPageEvents()
    data object OnStop : QuranPageEvents()
    data object OnSurasClick: QuranPageEvents()
    data object OnBookmarksClick: QuranPageEvents()
    data object OnSearchClick: QuranPageEvents()
    data object OnSettingsClick: QuranPageEvents()
    data object OnShareClick: QuranPageEvents()
    data object OnPageClick : QuranPageEvents()
    data object OnColorClick : QuranPageEvents()
    data object OnBookmarkLongClick : QuranPageEvents()
    data class OnShareDialogAction(val action: DialogActions) : QuranPageEvents()
    data class OnBookmarkDialogAction(val action: DialogActionsWithQuery) : QuranPageEvents()

}