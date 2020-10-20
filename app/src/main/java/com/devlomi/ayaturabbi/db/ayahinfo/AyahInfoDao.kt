package com.devlomi.ayaturabbi.db.ayahinfo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.devlomi.ayaturabbi.db.quran_ar.entities.ShareTextEntity

@Dao
interface AyahInfoDao {
    @Insert
    suspend fun insert(ayahInfo: AyahInfo)

    @Query("SELECT * FROM glyphs")
    suspend fun getAll(): List<AyahInfo>


    @Query("SELECT *  FROM glyphs WHERE page_number == :pageNumber ORDER BY sura_number, ayah_number,position  ")
    suspend fun getVersesBoundsForPage(pageNumber: Int): List<AyahInfo>

    @Query("SELECT page_number FROM glyphs WHERE sura_number == :surahNumber LIMIT 1")
    suspend fun getPageNumberBySurahNumber(surahNumber: Int): Int

    @Query("SELECT page_number FROM glyphs WHERE sura_number == :surahNumber AND ayah_number == :ayahNumber  LIMIT 1")
    suspend fun getPageNumberBySurahAndAyahNumber(surahNumber: Int, ayahNumber: Int): Int

    @Query("SELECT DISTINCT ayah_number FROM glyphs WHERE page_number == :pageNumber")
    suspend fun getAyatInPage(pageNumber: Int): List<Int>


}