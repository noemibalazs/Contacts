package com.example.contacts.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.contacts.model.User

@Database(entities = [User::class], exportSchema = false, version = 1)
@TypeConverters(
    UserNameConverter::class,
    UserLocationConverter::class,
    UserPictureConverter::class,
    UserIdConverter::class
)
abstract class UserDataBase : RoomDatabase() {

    abstract fun usersDao(): UsersDAO
}