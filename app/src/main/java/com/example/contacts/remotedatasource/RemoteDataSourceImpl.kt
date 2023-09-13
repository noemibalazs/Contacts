package com.example.contacts.remotedatasource

import com.example.contacts.model.RemoteUser
import com.example.contacts.network.RandomAPI
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class RemoteDataSourceImpl @Inject constructor(
    private val randomAPI: RandomAPI
) : RemoteDataSource {

    override fun getRandomUsers(results: Int): Single<List<RemoteUser>> =
        Single.just(true)
            .flatMap {
                randomAPI.getRandomUsers(results)
            }
            .map { it.results }

    override fun getUser(seed: String): Single<RemoteUser> =
        Single.just(true)
            .flatMap {
                randomAPI.getUser(seed)
            }
            .map { it.results.first() }

}