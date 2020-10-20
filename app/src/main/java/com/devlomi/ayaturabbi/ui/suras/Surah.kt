package com.devlomi.ayaturabbi.ui.suras

import androidx.recyclerview.widget.DiffUtil

data class Surah(val surahName: String, val surahNumber: Int) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Surah>() {
            override fun areContentsTheSame(oldItem: Surah, newItem: Surah): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Surah, newItem: Surah): Boolean {
                return oldItem.surahNumber == newItem.surahNumber
            }
        }
    }
}