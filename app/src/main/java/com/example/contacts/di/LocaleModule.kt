package com.example.contacts.di

import android.content.Context
import androidx.room.Room
import com.example.contacts.localedatasource.LocaleDataSource
import com.example.contacts.localedatasource.LocaleDataSourceImpl
import com.example.contacts.room.*
import com.example.contacts.room.UserLocationConverter
import com.example.contacts.room.UserNameConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class LocaleModule {

    @Singleton
    @Provides
    fun providesUsersDataBase(
        @ApplicationContext context: Context,
        userNameConverter: UserNameConverter,
        userLocationConverter: UserLocationConverter,
        userPictureConverter: UserPictureConverter,
        userIdConverter: UserIdConverter
    ): UserDataBase =
        Room.databaseBuilder(context, UserDataBase::class.java, "user_db")
            .addTypeConverter(userNameConverter)
            .addTypeConverter(userLocationConverter)
            .addTypeConverter(userPictureConverter)
            .addTypeConverter(userIdConverter)
            .build()

    @Provides
    @Singleton
    fun provideUserDao(userDataBase: UserDataBase): UsersDAO = userDataBase.usersDao()

    @Provides
    @Singleton
    fun providesLocalDataSource(usersDAO: UsersDAO): LocaleDataSource =
        LocaleDataSourceImpl(usersDAO)
}