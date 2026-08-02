package com.devlomi.shared.data.db.bookmark

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey
    val pageNumber: Int,
    val surahName: String,
    val timestamp: Long,
    val note: String? = null
) {



}