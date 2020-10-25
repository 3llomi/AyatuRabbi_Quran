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
            .also { Log.d("3llomi", "it $it") }


    }

    fun getQuranImageFile(pageNumber: Int): String {
        return quranImagesDataSource.getQuranImagesPathForPage(pageNumber)
    }

    companion object {
        private const val SEPARATOR = " ✵ "
    }

}