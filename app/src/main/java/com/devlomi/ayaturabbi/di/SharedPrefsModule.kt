package com.devlomi.ayaturabbi.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SharedPrefsModule {
    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
}