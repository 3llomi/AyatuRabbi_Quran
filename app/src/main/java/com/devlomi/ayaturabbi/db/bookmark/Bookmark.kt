package com.devlomi.ayaturabbi.db.bookmark

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.devlomi.ayaturabbi.util.DateFormatter

@Entity
data class Bookmark(
    @PrimaryKey
    val pageNumber: Int,
    val surahName: String,
    val timestamp: Long,
    val note: String? = null
) {

    @Ignore
    var formattedTimestamp: String = DateFormatter.formatDate(timestamp)
        private set

    companion object {
        val diffCallBack = object : DiffUtil.ItemCallback<Bookmark>() {
            override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean {
                return oldItem.pageNumber == newItem.pageNumber
            }
        }
    }
}