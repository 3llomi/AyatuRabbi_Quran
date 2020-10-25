package com.devlomi.ayaturabbi.db.ayahinfo

import android.graphics.RectF
import androidx.room.*


@Entity(
    tableName = "glyphs", indices = [Index("sura_number", "ayah_number", name = "sura_ayah_idx", unique = false), Index("page_number", name = "page_idx", unique = false)]
)
data class AyahInfo(

    @PrimaryKey val glyph_id: Int,

    val page_number: Int,
    val line_number: Int,
    val sura_number: Int,
    val ayah_number: Int,
    val position: Int,
    val min_x: Int,
    val max_x: Int,
    val min_y: Int,
    val max_y: Int,

    )
