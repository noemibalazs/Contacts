package com.example.contacts.localedatasource

import com.example.contacts.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LocaleDataSource {

    fun observeUsers(): Observable<List<User>>

    fun saveUsers(users: List<User>): Completable

    fun clearUsers(): Completable
}