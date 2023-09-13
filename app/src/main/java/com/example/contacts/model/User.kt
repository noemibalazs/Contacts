package com.example.contacts.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.contacts.utils.USERS_DB
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Entity(USERS_DB)
@Keep
@Serializable
data class User(
    @PrimaryKey
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: UserName,
    @SerialName("location")
    val location: UserLocation,
    @SerialName("cell")
    val mobileNumber: String,
    @SerialName("phone")
    val phoneNumber: String,
    @SerialName("picture")
    val picture: UserPicture,
    @SerialName("id")
    val id: UserId
)

@Keep
@Serializable
data class UserName(
    @SerialName("title")
    val title: String,
    @SerialName("first")
    val firstName: String,
    @SerialName("last")
    val lastName: String
)

@Keep
@Serializable
data class UserLocation(
    @SerialName("street")
    val street: UserStreet,
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String,
    @SerialName("country")
    val country: String?
)

@Keep
@Serializable
data class UserStreet(
    @SerialName("number")
    val number: Int,
    @SerialName("name")
    val name: String
)

@Keep
@Serializable
data class UserPicture(
    @SerialName("large")
    val large: String,
    @SerialName("medium")
    val medium: String,
    @SerialName("thumbnail")
    val thumbNail: String
)

@Keep
@Serializable
data class UserId(
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String
)

internal fun RemoteUser.toUser() = User(
    name = name.toUserName(),
    location = location.toUserLocation(),
    email = email,
    mobileNumber = mobileNumber,
    phoneNumber = phoneNumber,
    picture = picture.toUUserPicture(),
    id = id.toUserId()
)

internal fun RemoteUserName.toUserName() = UserName(
    title = title,
    firstName = firstName,
    lastName = lastName
)

internal fun RemoteUserLocation.toUserLocation() = UserLocation(
    street = street.toUserStreet(),
    city = city,
    state = state,
    country = country
)

internal fun RemoteUserStreet.toUserStreet() = UserStreet(
    number = number,
    name = name
)

internal fun RemoteUserPicture.toUUserPicture() = UserPicture(
    large = large,
    medium = medium,
    thumbNail = thumbNail
)

internal fun RemoteUserId.toUserId() = UserId(
    name = name,
    value = value.takeUnless { !it.isNullOrBlank() } ?: ""
)