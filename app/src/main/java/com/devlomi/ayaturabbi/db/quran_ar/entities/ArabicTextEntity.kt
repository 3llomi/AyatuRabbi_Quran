package com.devlomi.ayaturabbi.db.quran_ar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arabic_text",primaryKeys = ["sura","ayah"])
data class ArabicTextEntity(
    val sura: Int,
    val ayah: Int,
    val text: String,
)