package com.devlomi.shared.domain.model

data class SearchResult(
    val surahName: String,
    val pageNumber: Int,
    val ayahNumber: Int,
    val foundText: String,
    val highlightedText: String
) {

}