package com.devlomi.ayaturabbi.db.quran_ar.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Suppress("ArrayInDataClass")
@Entity(tableName = "verses_segdir",primaryKeys = ["level","idx"])
data class VersesSegDirEntity(
    val level: Int,
    val idx: Int,
    val start_block: Int?,
    val leaves_end_block: Int?,
    val end_block: Int?,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val root: ByteArray?,
)