package com.example.contacts.repository

import com.example.contacts.localedatasource.LocaleDataSource
import com.example.contacts.model.User
import com.example.contacts.model.toUser
import com.example.contacts.remotedatasource.RemoteDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localeDataSource: LocaleDataSource
) : UserRepository {

    override fun getUsers(results: Int): Single<List<User>> =
        Single.just(true)
            .flatMap {
                remoteDataSource.getRandomUsers(results)
            }
            .flatMap { remoteUsers ->
                val users = remoteUsers.map { it.toUser() }
                localeDataSource.saveUsers(users)
                    .toSingle { users }
            }

    override fun getUser(seed: String): Single<User> =
        Single.just(true)
            .flatMap {
                remoteDataSource.getUser(seed)
            }
            .map { remoteUser -> remoteUser.toUser() }

    override fun observeUsers(): Observable<List<User>> = localeDataSource.observeUsers()

    override fun clearUserDataBase(): Completable = localeDataSource.clearUsers()
}
