package com.devlomi.ayaturabbi.db.quran_ar.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class PropertiesEntity(
    @PrimaryKey
    val property: String,
    val value: String,
)