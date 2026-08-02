package com.devlomi.shared.ui.quran_page

import com.devlomi.shared.domain.PageColors
import com.devlomi.shared.domain.model.QuranPageItem

data class QuranPageState(
    val quranPages:List<QuranPageItem> = listOf(),
    val backgroundColor: String = PageColors.DKBLUE,
    val useWhiteColor: Boolean = false,
    val currentIndex:Int = 0,
    val isBookmarked:Boolean = false,
    val shareText:String? = null,
    val shareImage:String? = null,
    val pageScale: Float = 0f,
    val showZoomSheet: Int? = null
) {
}