package com.devlomi.ayaturabbi.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.devlomi.ayaturabbi.settings.SettingsRepository
import com.devlomi.ayaturabbi.db.DBFileNames
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDB
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
import com.devlomi.ayaturabbi.db.bookmark.BookmarkDB
import com.devlomi.ayaturabbi.db.quran_ar.QuranDB
import com.devlomi.ayaturabbi.db.quran_ar.QuranDBDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DBModule {


    @Singleton
    @Provides
    fun provideAyahInfoDb(
        @ApplicationContext context: Context,
        settingsRepository: SettingsRepository
    ): AyahInfoDB {
        val width = settingsRepository.deviceWidth()

        val file = File(context.filesDir, DBFileNames.ayahInfoNameDbPath(width))
        return Room.databaseBuilder(context, AyahInfoDB::class.java, AyahInfoDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    @Singleton
    @Provides
    fun provideAyahInfoDAO(ayahInfoDB: AyahInfoDB): AyahInfoDao {
        return ayahInfoDB.ayahInfoDao()
    }


    @Singleton
    @Provides
    fun provideQuranDb(
        @ApplicationContext context: Context
    ): QuranDB {

        val file = File(context.filesDir, DBFileNames.quranDbPath)
        return Room.databaseBuilder(context, QuranDB::class.java, QuranDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .createFromFile(file)
            .build()
    }

    @Singleton
    @Provides
    fun provideQuranDbDAO(quranDb: QuranDB): QuranDBDao {
        return quranDb.quranDBDao()
    }


    @Singleton
    @Provides
    fun provideBookmarkDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, BookmarkDB::class.java, BookmarkDB.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()


    @Singleton
    @Provides
    fun provideBookmarkDao(bookmarkDB: BookmarkDB) = bookmarkDB.bookmarkDao()

}