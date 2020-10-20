package com.devlomi.ayaturabbi.db.quran_ar

import android.util.Log
import androidx.room.Query
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.quran_ar.entities.ShareTextEntity
import com.devlomi.ayaturabbi.ui.quran_page.QuranImagesDataSource
import com.devlomi.ayaturabbi.ui.quran_page.QuranPageDataSource
import javax.inject.Inject

class QuranRepository @Inject constructor(
    private val quranDBDao: QuranDBDao,
    private val ayahInfoDao: AyahInfoDao,
    private val quranImagesDataSource: QuranImagesDataSource
) {

    suspend fun getShareTextForPage(pageNumber: Int): String {

        val ayatInPage = ayahInfoDao.getAyatInPage(pageNumber)
        val surahNumber = 2 //TODO GET REAL SURAH NUMBER
//            val ayatNumbers = ayatInPage.map { it.ayah_number }
        val shareTextBySurah = quranDBDao.getShareTextBySurah(surahNumber, ayatInPage)
        return shareTextBySurah.joinToString(separator = SEPARATOR)


    }

    fun getQuranImageFile(pageNumber: Int): String {
        return quranImagesDataSource.getQuranImagesPathForPage(pageNumber)
    }

    companion object {
        const val SEPARATOR = " ✵ "
    }

}