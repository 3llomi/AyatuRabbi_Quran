package com.devlomi.shared.data.db.quran_ar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


expect class VersesContentEntity: VersesContentEntityInterface {
    override val docid: Int
    override val c0sura: String
    override val c1ayah: String
    override val c2text: String
    override val c3primary: String?
}

interface VersesContentEntityInterface {
    val docid: Int
    val c0sura: String
    val c1ayah: String
    val c2text: String
    val c3primary: String?
}