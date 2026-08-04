package com.devlomi.shared.ui.quran_page

import com.devlomi.shared.domain.PageColors
import com.devlomi.shared.domain.model.QuranPageItem
import com.devlomi.shared.ui.components.DialogState
import com.devlomi.shared.ui.components.InputDialogState

data class QuranPageState(
    val quranPages:List<QuranPageItem> = listOf(),
    val backgroundColor: String = PageColors.DKBLUE,
    val showOptionsPanel: Boolean = false,
    val showColorsPanel: Boolean = false,
    val useWhiteColor: Boolean = false,
    val currentIndex:Int = 0,
    val isBookmarked:Boolean = false,
    val shareText:String? = null,
    val shareImage:String? = null,
    val pageScale: Float = 0f,
    val pageScaleSliderValue: Float = 0f,
    val showZoomSheet: Boolean = false,
    //TODO IS IT THE SAME AS PAGE SCALE? IF YES REMOVE IT
    val zoomPercentage: Float = 40f,
    val shareTypeDialogState: DialogState = DialogState(),
    val bookmarkDialogState: InputDialogState = InputDialogState(),

    ) {
}