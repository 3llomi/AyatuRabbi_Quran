package com.devlomi.shared.data.quran_datasource



class QuranImagesDataSource (
    private val quranImagesPathProvider: QuranImagePathProvider
) {
    companion object {
        const val EXTENSION = ".png"
        const val PAGE_PREFIX = "page"
    }



    fun getQuranImagesPathForPage(pageNumber: Int): String {
        val number = getNumber(pageNumber)
        val fileName = "$PAGE_PREFIX${number}$EXTENSION"
        val path = "${quranImagesPathProvider.getQuranImageBasePath()}/$fileName"
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