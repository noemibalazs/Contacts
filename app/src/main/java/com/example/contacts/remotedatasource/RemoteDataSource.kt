package com.example.contacts.remotedatasource

import com.example.contacts.model.RemoteUser
import io.reactivex.rxjava3.core.Single

interface RemoteDataSource {

    fun getRandomUsers(results: Int): Single<List<RemoteUser>>

    fun getUser(seed: String): Single<RemoteUser>
}