package com.devlomi.shared.data.quran_datasource

import com.devlomi.shared.common.DirConstants


class QuranImagesDataSource (
    private val dirConstants: DirConstants
) {
    companion object {
        const val EXTENSION = ".png"
        const val PAGE_PREFIX = "page"
    }



    fun getQuranImagesPathForPage(pageNumber: Int): String {
        val number = getNumber(pageNumber)
        val fileName = "$PAGE_PREFIX${number}$EXTENSION"
        val path = "${dirConstants.getQuranImageBasePath()}/$fileName"
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