package com.devlomi.ayaturabbi.datasource.quran_datasource

import java.io.File
import javax.inject.Inject

class QuranImagesDataSource @Inject constructor(
    private val quranImagesPath:File
) {
    companion object {
        const val EXTENSION = ".png"
        const val PAGE_PREFIX = "page"
    }



    fun getQuranImagesPathForPage(pageNumber: Int): String {
        val number = getNumber(pageNumber)
        val fileName = "$PAGE_PREFIX${number}$EXTENSION"
        val path = "${quranImagesPath.path}/$fileName"
        return path
    }

    private fun getNumber(number: Int): String {
        val numberStr = "$number"
        return when (numberStr.length) {
            1 -> "00$number"
            2 -> "0$number"
            else -> numberStr
        }
    }
}