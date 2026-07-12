package com.devlomi.shared.db.ayahinfo


class AyahInfoRepository (private val ayahInfoDao: AyahInfoDao) {
    suspend fun getPageNumberBySurahNumber(surahNumber: Int) =
        ayahInfoDao.getPageNumberBySurahNumber(surahNumber)


}