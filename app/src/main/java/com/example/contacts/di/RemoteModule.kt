package com.example.contacts.di

import com.example.contacts.network.RandomAPI
import com.example.contacts.remotedatasource.RemoteDataSource
import com.example.contacts.remotedatasource.RemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
internal class RemoteModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(randomAPI: RandomAPI): RemoteDataSource =
        RemoteDataSourceImpl(randomAPI)
}