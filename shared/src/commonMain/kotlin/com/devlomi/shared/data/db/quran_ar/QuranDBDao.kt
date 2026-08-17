package com.devlomi.shared.data.db.quran_ar

import androidx.room.Dao
import androidx.room.Query
import com.devlomi.shared.data.db.quran_ar.entities.ArabicTextEntity
import com.devlomi.shared.data.db.quran_ar.entities.ShareTextEntity
import com.devlomi.shared.data.db.quran_ar.entities.VersesContentEntity

@Dao
/*
on IOS, the FTS table verses_content which contains a reserved keyword '_content'
therefore we had to modify the table from 'verses_content' to 'verses_contents'
and we had to create two DAOs and two entities for the same table, one for android and one for IOS
the iOS version files available on the server are the same, except for the table name
 */
expect interface QuranDBDao {

    suspend fun searchForAyah(query: String): List<VersesContentEntity>



    //get all ayat that that matches sura & aya number
    suspend fun getShareTextBySurah(
        surahNumbers: List<Int>,
        ayatNumbersIntPage: List<Int>
    ): List<ShareTextEntity>
}