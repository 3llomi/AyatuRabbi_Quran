package com.devlomi.shared.data.db.quran_ar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verses_content")
actual data class VersesContentEntity(
    @PrimaryKey
    actual override val docid: Int,
    actual override val c0sura: String,
    actual override val c1ayah: String,
    actual override val c2text: String,
    actual override val c3primary: String?,
): VersesContentEntityInterface