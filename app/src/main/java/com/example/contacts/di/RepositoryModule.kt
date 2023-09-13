package com.example.contacts.di

import com.example.contacts.localedatasource.LocaleDataSource
import com.example.contacts.remotedatasource.RemoteDataSource
import com.example.contacts.repository.UserRepository
import com.example.contacts.repository.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [RemoteModule::class, LocaleModule::class])
@InstallIn(SingletonComponent::class)
internal class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        remoteDataSource: RemoteDataSource,
        localeDataSource: LocaleDataSource
    ): UserRepository =
        UserRepositoryImpl(remoteDataSource, localeDataSource)
}