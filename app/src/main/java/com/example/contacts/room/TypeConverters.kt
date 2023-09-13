package com.example.contacts.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.contacts.model.UserId
import com.example.contacts.model.UserLocation
import com.example.contacts.model.UserName
import com.example.contacts.model.UserPicture
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ProvidedTypeConverter
internal class UserNameConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun fromUserName(userName: UserName): String =
        json.encodeToString(UserName.serializer(), userName)

    @TypeConverter
    fun toUserName(userName: String): UserName =
        json.decodeFromString(UserName.serializer(), userName)
}


@ProvidedTypeConverter
internal class UserLocationConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun fromUserLocation(userLocation: UserLocation): String =
        json.encodeToString(UserLocation.serializer(), userLocation)

    @TypeConverter
    fun toUserLocation(userLocation: String): UserLocation =
        json.decodeFromString(UserLocation.serializer(), userLocation)
}

@ProvidedTypeConverter
internal class UserPictureConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun fromUsePicture(userPicture: UserPicture): String =
        json.encodeToString(UserPicture.serializer(), userPicture)

    @TypeConverter
    fun toUserPicture(userPicture: String): UserPicture =
        json.decodeFromString(UserPicture.serializer(), userPicture)
}

@ProvidedTypeConverter
internal class UserIdConverter @Inject constructor(private val json: Json) {

    @TypeConverter
    fun fromUserId(userId: UserId): String =
        json.encodeToString(UserId.serializer(), userId)

    @TypeConverter
    fun toUserId(userId: String): UserId =
        json.decodeFromString(UserId.serializer(), userId)
}