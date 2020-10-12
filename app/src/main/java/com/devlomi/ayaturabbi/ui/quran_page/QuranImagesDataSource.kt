package com.devlomi.ayaturabbi.ui.quran_page

import android.util.Log
import java.io.File
import javax.inject.Inject

class QuranImagesDataSource @Inject constructor() {
    companion object {
        const val EXTENSION = ".png"
        const val PAGE_PREFIX = "page"
        const val PAGES_COUNT = 604
    }

    fun getQuranImagesPaths(quranDataDirectoryPath: String): List<String> {
        val list = mutableListOf<String>()
        for (i in 1 until PAGES_COUNT) {
            val number = getNumber(i)
            val fileName = "${PAGE_PREFIX}${number}$EXTENSION"
            val path = "$quranDataDirectoryPath/$fileName"
            Log.d("3llomi","path is $path")
            list.add(path)
        }
        return list
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