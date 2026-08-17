package com.devlomi.shared.data.db.quran_ar

import androidx.room.Dao
import androidx.room.Query
import com.devlomi.shared.data.db.quran_ar.entities.ShareTextEntity
import com.devlomi.shared.data.db.quran_ar.entities.VersesContentEntity

@Dao
actual interface QuranDBDao {
    @Query(value = "SELECT * FROM verses_contents WHERE c2text LIKE '%' || :query || '%'")
    actual suspend fun searchForAyah(query: String): List<VersesContentEntity>

    @Query(value = "SELECT * FROM share_text WHERE sura IN (:surahNumbers) AND ayah IN (:ayatNumbersIntPage) ORDER BY sura ")
    actual suspend fun getShareTextBySurah(
        surahNumbers: List<Int>,
        ayatNumbersIntPage: List<Int>
    ): List<ShareTextEntity>
}