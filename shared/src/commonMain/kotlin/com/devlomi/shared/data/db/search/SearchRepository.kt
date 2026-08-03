package com.devlomi.shared.data.db.search

import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.surah_names
import com.devlomi.shared.domain.model.SearchResult
import org.jetbrains.compose.resources.getStringArray


class SearchRepository(

    private val ayahInfoDao: com.devlomi.shared.data.db.ayahinfo.AyahInfoDao,
    private val quranDBDao: com.devlomi.shared.data.db.quran_ar.QuranDBDao,
) {


    suspend fun searchForAyah(query: String): List<SearchResult> {

        val foundResults = quranDBDao.searchForAyah(query)

        val suras = getStringArray(Res.array.surah_names)

        return foundResults.map {
            val suraNumber = it.c0sura!!.toInt()
            val surah = suras.getOrNull(suraNumber - 1) ?: ""

            val foundPageNumber = ayahInfoDao.getPageNumberBySurahAndAyahNumber(suraNumber,it.c1ayah!!.toInt())


            SearchResult(surah, foundPageNumber, it.c1ayah!!.toInt(), it.c2text ?: "", query)
        }

    }
}