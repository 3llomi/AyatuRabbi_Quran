package com.devlomi.ayaturabbi.di

import com.devlomi.ayaturabbi.network.APIWebService
import com.devlomi.ayaturabbi.network.DownloadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(webService: APIWebService) = DownloadRepository(webService)
}