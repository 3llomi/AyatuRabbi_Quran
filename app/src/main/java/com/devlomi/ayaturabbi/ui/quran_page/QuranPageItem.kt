package com.devlomi.ayaturabbi.ui.quran_page

import android.util.Log
import androidx.recyclerview.widget.DiffUtil

data class QuranPageItem(
    val imageFilePath: String,
    val pageNumber: Int,
    val pageNumberLocalized: String,
    val surahName: String,
    val juzoaNumber: Int? = null,
    val juzoaNumberLocalized: String? = null,
    val juzoaNumberText: String? = null
) {
    companion object {
        val diffCallback = object :
            DiffUtil.ItemCallback<QuranPageItem>() {
            override fun areItemsTheSame(oldItem: QuranPageItem, newItem: QuranPageItem): Boolean {
                Log.d("3llomi","areItemsTheSame")

                return oldItem.pageNumber == newItem.pageNumber
            }

            override fun areContentsTheSame(
                oldItem: QuranPageItem,
                newItem: QuranPageItem
            ): Boolean {
                Log.d("3llomi","old item is $oldItem newItem $newItem")
                return oldItem == newItem
            }

        }
    }
}