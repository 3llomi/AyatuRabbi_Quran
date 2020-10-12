package com.devlomi.ayaturabbi.db.quran_ar

import androidx.room.Dao
import androidx.room.Query
import com.devlomi.ayaturabbi.db.quran_ar.entities.ArabicTextEntity

@Dao
interface QuranDBDao {
    @Query("SELECT * FROM arabic_text")
    suspend fun getAll(): List<ArabicTextEntity>
}