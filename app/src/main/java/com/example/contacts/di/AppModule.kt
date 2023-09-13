package com.example.contacts.di

import android.app.Application
import com.example.contacts.app.App
import com.example.contacts.utils.InputValidation
import com.example.contacts.utils.InputValidationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    @AppInstance
    fun providesApplication(app: App): Application = app


    @Provides
    @Singleton
    fun providesInputValidation(): InputValidation = InputValidationImpl()
}