package com.devlomi.ayaturabbi.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object SharedPrefsModule {
    @Singleton
    @Provides
    fun provideSharedPrefs(@ApplicationContext context: Context) =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
}