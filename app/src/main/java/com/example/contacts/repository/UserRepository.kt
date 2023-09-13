package com.example.contacts.repository

import com.example.contacts.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun getUsers(results: Int): Single<List<User>>

    fun getUser(seed: String): Single<User>

    fun observeUsers(): Observable<List<User>>

    fun clearUserDataBase():Completable
}