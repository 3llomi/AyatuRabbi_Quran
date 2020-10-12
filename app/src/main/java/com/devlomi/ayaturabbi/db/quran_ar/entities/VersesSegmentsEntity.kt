package com.devlomi.ayaturabbi.db.quran_ar.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Suppress("ArrayInDataClass")
@Entity(tableName = "verses_segments")
data class VersesSegmentsEntity(
    @PrimaryKey
    val blockid: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val block: ByteArray?,
)