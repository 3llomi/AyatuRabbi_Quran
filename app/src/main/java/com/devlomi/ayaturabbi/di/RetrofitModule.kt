package com.devlomi.ayaturabbi.di

import com.devlomi.ayaturabbi.network.APIWebService
import com.devlomi.ayaturabbi.network.DownloadRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit() =
        Retrofit.Builder()
            .baseUrl(DownloadRepository.BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())


    @Singleton
    @Provides
    fun provideWebservice(retrofit: Retrofit.Builder) =
        retrofit.build().create(APIWebService::class.java)





}