package com.example.contacts.network

import com.example.contacts.model.RemoteResults
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomAPI {

    @GET("?")
    fun getRandomUsers(@Query("results") results: Int): Single<RemoteResults>

    @GET("?")
    fun getUser(@Query("seed") seed: String): Single<RemoteResults>
}