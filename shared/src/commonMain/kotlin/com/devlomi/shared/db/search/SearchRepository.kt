package com.devlomi.shared.db.search

import com.devlomi.shared.SearchResult
import com.devlomi.shared.SharedString
import com.devlomi.shared.StringArrays
import com.devlomi.shared.db.ayahinfo.AyahInfoDao
import com.devlomi.shared.db.quran_ar.QuranDBDao


class SearchRepository(
    private val sharedString: SharedString,
    private val ayahInfoDao: AyahInfoDao,
    private val quranDBDao: QuranDBDao,
) {


    suspend fun searchForAyah(query: String): List<SearchResult> {

        val foundResults = quranDBDao.searchForAyah(query)

        val suras = sharedString.getStringArray(StringArrays.SurahNames)

        return foundResults.map {
            val suraNumber = it.c0sura!!.toInt()
            val surah = suras.getOrNull(suraNumber - 1) ?: ""

            val foundPageNumber = ayahInfoDao.getPageNumberBySurahAndAyahNumber(suraNumber,it.c1ayah!!.toInt())


            SearchResult(surah, foundPageNumber, it.c1ayah!!.toInt(), it.c2text ?: "", query)
        }

    }
}