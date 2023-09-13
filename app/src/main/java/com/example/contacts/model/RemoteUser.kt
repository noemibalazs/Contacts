package com.example.contacts.model

import androidx.annotation.Keep
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Keep
@Serializable
data class RemoteResults(
    @SerialName("results")
    val results: List<RemoteUser>
)

@Keep
@Serializable
data class RemoteUser(
    @SerialName("name")
    val name: RemoteUserName,
    @SerialName("location")
    val location: RemoteUserLocation,
    @SerialName("email")
    val email: String,
    @SerialName("cell")
    val mobileNumber: String,
    @SerialName("phone")
    val phoneNumber: String,
    @SerialName("picture")
    val picture: RemoteUserPicture,
    @SerialName("id")
    val id: RemoteUserId
)

@Keep
@Serializable
data class RemoteUserName(
    @SerialName("title")
    val title: String,
    @SerialName("first")
    val firstName: String,
    @SerialName("last")
    val lastName: String
)

@Keep
@Serializable
data class RemoteUserLocation(
    @SerialName("street")
    val street: RemoteUserStreet,
    @SerialName("city")
    val city: String,
    @SerialName("state")
    val state: String,
    @SerialName("country")
    val country: String?
)

@Keep
@Serializable
data class RemoteUserStreet(
    @SerialName("number")
    val number: Int,
    @SerialName("name")
    val name: String
)

@Keep
@Serializable
data class RemoteUserPicture(
    @SerialName("large")
    val large: String,
    @SerialName("medium")
    val medium: String,
    @SerialName("thumbnail")
    val thumbNail: String
)

@Keep
@Serializable
data class RemoteUserId(
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String? = null
)
