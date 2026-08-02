package com.devlomi.shared.data.db.ayahinfo


class AyahInfoRepository (private val ayahInfoDao: com.devlomi.shared.data.db.ayahinfo.AyahInfoDao) {
    suspend fun getPageNumberBySurahNumber(surahNumber: Int) =
        ayahInfoDao.getPageNumberBySurahNumber(surahNumber)


}