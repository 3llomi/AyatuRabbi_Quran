package com.devlomi.ayaturabbi.db.quran_ar

import androidx.room.Dao
import androidx.room.Query
import com.devlomi.ayaturabbi.db.quran_ar.entities.ArabicTextEntity
import com.devlomi.ayaturabbi.db.quran_ar.entities.ShareTextEntity
import com.devlomi.ayaturabbi.db.quran_ar.entities.VersesContentEntity

@Dao
interface QuranDBDao {

    @Query("SELECT * FROM verses_content WHERE c2text LIKE '%' || :query || '%'")
    suspend fun searchForAyah(query: String): List<VersesContentEntity>


    //get all ayat that that matches sura & aya number
    @Query("SELECT text FROM share_text WHERE sura == :surahNumber AND ayah IN (:ayatNumbersIntPage) ")
    suspend fun getShareTextBySurah(
        surahNumber: Int,
        ayatNumbersIntPage: List<Int>
    ): List<String>
}