package com.devlomi.shared.data.db.quran_ar

import com.devlomi.shared.data.quran_datasource.QuranImagesDataSource

class QuranRepository (
    private val quranDBDao: QuranDBDao,
    private val ayahInfoDao: com.devlomi.shared.data.db.ayahinfo.AyahInfoDao,
    private val quranImagesDataSource: QuranImagesDataSource
) {

    suspend fun getShareTextForPage(pageNumber: Int): String {

        //first gell all ayat in a Page
        val ayatInPage = ayahInfoDao.getAyatInPage(pageNumber)


        /*
        now group all ayat in this page with surah number
        then remove duplicate ayah numbers
        then getShareText for every ayah that that has the surah number
        lastly convert listOfLists to a single List (Flatten)
        and get the text out of it
        at the end join it to a sharable string with a separator
        */
        return ayatInPage.groupBy {
            it.sura_number
        }.mapValues { it.value.map { ayahInfo -> ayahInfo.ayah_number }.distinct() }
            .map {
                quranDBDao.getShareTextBySurah(listOf(it.key), it.value)
            }.flatten().map { it.text }.joinToString(separator = SEPARATOR)


    }

    fun getQuranImageFile(pageNumber: Int): String {
        return quranImagesDataSource.getQuranImagesPathForPage(pageNumber)
    }

    companion object {
        private const val SEPARATOR = " ✵ "
    }

}