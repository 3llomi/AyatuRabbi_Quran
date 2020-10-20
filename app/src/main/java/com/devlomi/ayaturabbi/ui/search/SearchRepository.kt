package com.devlomi.ayaturabbi.ui.search

import android.content.Context
import android.util.Log
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.quran_ar.QuranDBDao
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SearchRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ayahInfoDao: AyahInfoDao,
    private val quranDBDao: QuranDBDao
) {

    private val suras = context.resources.getStringArray(R.array.surah_names)

    suspend fun searchForAyah(query: String): List<SearchResult> {

        val foundResults = quranDBDao.searchForAyah(query)



        return foundResults.map {
            val suraNumber = it.c0sura!!.toInt()
            val surah = suras.getOrNull(suraNumber - 1) ?: ""

            val foundPageNumber = ayahInfoDao.getPageNumberBySurahAndAyahNumber(suraNumber,it.c1ayah!!.toInt())


            SearchResult(surah, foundPageNumber, it.c1ayah!!.toInt(), it.c2text ?: "", query)
        }

    }
}