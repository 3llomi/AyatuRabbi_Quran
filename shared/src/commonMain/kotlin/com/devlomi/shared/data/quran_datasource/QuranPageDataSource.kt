package com.devlomi.shared.data.quran_datasource

import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.ajzaa_number_text
import ayaturabbi.shared.generated.resources.surah_names
import com.devlomi.shared.common.DecimalFormat
import com.devlomi.shared.domain.model.QuranPageItem
import org.jetbrains.compose.resources.getStringArray


class QuranPageDataSource(
    private val quranImagesDataSource: QuranImagesDataSource
) {
    lateinit var surahsNames: Array<String>
    lateinit var ajzaaNumbersText: Array<String>


    companion object {
        const val PAGES_COUNT = 604
    }

    //TODO INIT THIS IN APP
    suspend fun init() {
        surahsNames = getStringArray(Res.array.surah_names).toTypedArray()
        ajzaaNumbersText = getStringArray(Res.array.ajzaa_number_text).toTypedArray()
    }

    fun getSuraForPageArray() = intArrayOf(
        1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
        2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
        3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
        4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
        5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7,
        7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8,
        8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10,
        10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 11,
        11, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12,
        13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 16,
        16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 17, 17, 17, 17, 17, 17, 17,
        17, 17, 17, 17, 17, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 19, 19, 19, 19,
        19, 19, 19, 19, 20, 20, 20, 20, 20, 20, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21,
        21, 21, 21, 22, 22, 22, 22, 22, 22, 22, 22, 22, 22, 23, 23, 23, 23, 23, 23, 23,
        23, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 25, 25, 25, 25, 25, 25, 25, 26, 26,
        26, 26, 26, 26, 26, 26, 26, 26, 27, 27, 27, 27, 27, 27, 27, 27, 27, 28, 28, 28,
        28, 28, 28, 28, 28, 28, 28, 28, 29, 29, 29, 29, 29, 29, 29, 29, 30, 30, 30, 30,
        30, 30, 31, 31, 31, 31, 32, 32, 32, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 34,
        34, 34, 34, 34, 34, 34, 35, 35, 35, 35, 35, 35, 36, 36, 36, 36, 36, 37, 37, 37,
        37, 37, 37, 37, 38, 38, 38, 38, 38, 38, 39, 39, 39, 39, 39, 39, 39, 39, 39, 40,
        40, 40, 40, 40, 40, 40, 40, 40, 41, 41, 41, 41, 41, 41, 42, 42, 42, 42, 42, 42,
        42, 43, 43, 43, 43, 43, 43, 44, 44, 44, 45, 45, 45, 45, 46, 46, 46, 46, 47, 47,
        47, 47, 48, 48, 48, 48, 48, 49, 49, 50, 50, 50, 51, 51, 51, 52, 52, 53, 53, 53,
        54, 54, 54, 55, 55, 55, 56, 56, 56, 57, 57, 57, 57, 58, 58, 58, 58, 59, 59, 59,
        60, 60, 60, 61, 62, 62, 63, 64, 64, 65, 65, 66, 66, 67, 67, 67, 68, 68, 69, 69,
        70, 70, 71, 72, 72, 73, 73, 74, 74, 75, 76, 76, 77, 78, 78, 79, 80, 81, 82, 83,
        83, 85, 86, 87, 89, 89, 91, 92, 95, 97, 98, 100, 103, 106, 109, 112
    )

    fun getPageForJuzArray() = intArrayOf(
        /*  1 - 10 */ 1, 22, 42, 62, 82, 102, 121, 142, 162, 182,
        /* 11 - 20 */ 202, 222, 242, 262, 282, 302, 322, 342, 362, 382,
        /* 21 - 30 */ 402, 422, 442, 462, 482, 502, 522, 542, 562, 582
    )

    private fun getJuzoaFromPage(page: Int): Int {
        val pageForJuzoa = getPageForJuzArray()
        for (i in pageForJuzoa.indices) {
            if (pageForJuzoa[i] > page) {
                return i
            } else if (pageForJuzoa[i] == page) {
                return i + 1
            }
        }
        return 30
    }


    fun getData(): List<QuranPageItem> {
        val list = mutableListOf<QuranPageItem>()
        for (i in 0 until PAGES_COUNT) {

            val quranPageItem =
                getQuranPageItemByIndex(
                    i,
                )

            list.add(quranPageItem)

        }
        return list
    }

    fun getQuranPageItemByIndex(
        index: Int,
    ): QuranPageItem {
        val pageNumber = index + 1

        val imageFilePath =
            quranImagesDataSource.getQuranImagesPathForPage(pageNumber)
        //if it's the current pageNumber fetch full data
        val surahNumber = getSuraForPageArray()[index]
        val surahName = surahsNames[surahNumber - 1]
        val juzoa = getJuzoaFromPage(pageNumber)
        val locale = "ar"
        val localizedJuzoaNumber =
            DecimalFormat.format(juzoa, locale)
        val juzoaNumberText = ajzaaNumbersText[juzoa - 1]

        return QuranPageItem(
            imageFilePath,
            pageNumber,
            DecimalFormat.format(pageNumber, locale),
            surahName,
            juzoa,
            localizedJuzoaNumber,
            juzoaNumberText
        )

    }
}