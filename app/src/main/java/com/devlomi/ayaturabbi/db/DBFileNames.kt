package com.devlomi.ayaturabbi.db

object DBFileNames {
    fun ayahInfoNameZipFile(width:Int) = "ayahinfo_${width}.zip"
    fun ayahInfoNameDbFile(width:Int) = "ayahinfo_${width}.db"
    fun ayahInfoNameDbPath(width:Int) = "db/ayahinfo_${width}.db"
    const val quranDbFileName = "quran_db.db"
    const val quranDbPath = "db/quran_db.db"
}