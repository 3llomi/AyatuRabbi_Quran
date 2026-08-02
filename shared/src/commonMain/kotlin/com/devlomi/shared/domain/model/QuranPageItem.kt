package com.devlomi.shared.domain.model

data class QuranPageItem(
    val imageFilePath: String,
    val pageNumber: Int,
    val pageNumberLocalized: String,
    val surahName: String,
    val juzoaNumber: Int? = null,
    val juzoaNumberLocalized: String? = null,
    val juzoaNumberText: String? = null
)