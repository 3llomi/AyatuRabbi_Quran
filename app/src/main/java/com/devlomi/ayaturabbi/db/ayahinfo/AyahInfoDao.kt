package com.devlomi.ayaturabbi.db.ayahinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AyahInfoDao {
    @Insert
    suspend fun insert(ayahInfo: AyahInfo)

    @Query("SELECT * FROM glyphs")
    suspend fun getAll(): List<AyahInfo>

    @Query("SELECT * FROM glyphs WHERE glyph_Id == :glyphId LIMIT 1")
    suspend fun getAyahInfoByGlyphId(glyphId: Int): AyahInfo



}