package com.devlomi.ayaturabbi.db.quran_ar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses_content")
data class VersesContentEntity(
    @PrimaryKey
    val docid: Int,
    val c0sura: String?,
    val c1ayah: String?,
    val c2text: String?,
    val c3primary: String?,
)