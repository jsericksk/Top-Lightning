package com.kproject.toplightning.di

import android.content.Context
import com.kproject.toplightning.data.local.repository.prefs.PreferenceRepository
import com.kproject.toplightning.data.local.repository.prefs.PreferenceRepositoryImpl
import com.kproject.toplightning.data.remote.MempoolApiService
import com.kproject.toplightning.data.remote.repository.MempoolRepository
import com.kproject.toplightning.data.remote.repository.MempoolRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMempoolApiService(): MempoolApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MempoolApiService.BASE_URL)
            .build()
            .create(MempoolApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMempoolRepository(mempoolApiService: MempoolApiService): MempoolRepository {
        return MempoolRepositoryImpl(mempoolApiService)
    }

    @Provides
    @Singleton
    fun providePreferenceRepository(@ApplicationContext applicationContext: Context): PreferenceRepository {
        return PreferenceRepositoryImpl(applicationContext)
    }
}