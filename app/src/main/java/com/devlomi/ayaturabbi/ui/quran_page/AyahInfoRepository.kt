package com.devlomi.ayaturabbi.ui.quran_page

import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.quran_ar.QuranDBDao
import javax.inject.Inject

//TODO DUPLICATE CLASSES AyahInfoRepository, delete one
class AyahInfoRepository @Inject constructor(private val ayahInfoDao: AyahInfoDao) {
    suspend fun getPageNumberBySurahNumber(surahNumber: Int) =
        ayahInfoDao.getPageNumberBySurahNumber(surahNumber)

    suspend fun getShareTextForPage(number: Int) {

    }
}