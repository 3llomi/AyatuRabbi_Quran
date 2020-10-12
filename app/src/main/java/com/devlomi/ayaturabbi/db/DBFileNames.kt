package com.devlomi.ayaturabbi.db

object DBFileNames {
    fun ayahInfoNameZipFile(width:Int) = "ayahinfo_${width}.zip"
    fun ayahInfoNameDbFile(width:Int) = "ayahinfo_${width}.db"
    fun ayahInfoNameDbPath(width:Int) = "databases/ayahinfo_${width}.db"
    const val quranDbFileName = "quran.ar.uthmani.v2.db"
    const val quranDbPath = "databases/quran.ar.uthmani.v2.db"
}