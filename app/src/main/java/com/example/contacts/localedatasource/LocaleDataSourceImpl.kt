package com.example.contacts.localedatasource

import com.example.contacts.model.User
import com.example.contacts.room.UsersDAO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

internal class LocaleDataSourceImpl @Inject constructor(
    private val usersDAO: UsersDAO
) : LocaleDataSource {

    override fun observeUsers(): Observable<List<User>> = usersDAO.observeUsers()

    override fun saveUsers(users: List<User>): Completable = usersDAO.saveUsers(users)

    override fun clearUsers(): Completable = usersDAO.clearUsers()
}