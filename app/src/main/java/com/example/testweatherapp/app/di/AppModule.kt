package com.example.testweatherapp.app.di

import android.content.Context
import com.example.testweatherapp.app.helpers.NetworkStatusProviderImpl
import com.example.testweatherapp.domain.network.NetworkStatusProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideNetworkStatusProvider(@ApplicationContext context: Context): NetworkStatusProvider {
        return NetworkStatusProviderImpl(context)
    }

}