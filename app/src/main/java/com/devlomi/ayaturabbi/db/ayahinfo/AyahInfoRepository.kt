package com.devlomi.ayaturabbi.db.ayahinfo

import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.quran_ar.QuranDBDao
import javax.inject.Inject

class AyahInfoRepository @Inject constructor(private val ayahInfoDao: AyahInfoDao) {
    suspend fun getPageNumberBySurahNumber(surahNumber: Int) =
        ayahInfoDao.getPageNumberBySurahNumber(surahNumber)


}