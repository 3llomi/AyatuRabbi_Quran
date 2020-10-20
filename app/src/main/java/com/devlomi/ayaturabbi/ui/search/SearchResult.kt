package com.devlomi.ayaturabbi.ui.search

import androidx.recyclerview.widget.DiffUtil

data class SearchResult(
    val surahName: String,
    val pageNumber: Int,
    val ayahNumber: Int,
    val foundText: String,
    val highlightedText: String
) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.ayahNumber == newItem.ayahNumber && oldItem.pageNumber == newItem.pageNumber && oldItem.highlightedText == newItem.highlightedText
            }

            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
                return oldItem.ayahNumber == newItem.ayahNumber
            }
        }
    }
}