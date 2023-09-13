package com.example.contacts.remotedatasource

import com.example.contacts.model.RemoteResults
import com.example.contacts.model.RemoteUser
import com.example.contacts.network.RandomAPI
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RemoteDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    @MockK
    private lateinit var randomAPI: RandomAPI

    private val result: RemoteResults = mockk()
    private val error: Exception = mockk()
    private val user: RemoteUser = mockk()

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSourceImpl(randomAPI)
    }

    @Test
    fun `test get random users and should be successful`() {
        every { randomAPI.getRandomUsers(12) } returns Single.just(result)
        every { result.results } returns listOf(user)

        remoteDataSource.getRandomUsers(12)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == listOf(user) }

        verify(exactly = 1) { randomAPI.getRandomUsers(12) }
    }

    @Test
    fun `test get random users and throws exception`() {
        every { randomAPI.getRandomUsers(12) } returns Single.error(error)

        remoteDataSource.getRandomUsers(12)
            .test()
            .assertNoValues()
            .assertNotComplete()
            .assertError(error)

        verify(exactly = 1) { randomAPI.getRandomUsers(12) }
    }

    @Test
    fun `test get user and should be successful`() {
        val seed = "seed"

        every { randomAPI.getUser(seed) } returns Single.just(result)
        every { result.results } returns listOf(user)

        remoteDataSource.getUser(seed)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == user }

        verify(exactly = 1) { randomAPI.getUser(seed) }
    }

    @Test
    fun `test get user and throws exception`() {
        val seed = "seed"

        every { randomAPI.getUser(seed) } returns Single.error(error)

        remoteDataSource.getUser(seed)
            .test()
            .assertNotComplete()
            .assertNoValues()
            .assertError(error)

        verify(exactly = 1) { randomAPI.getUser(seed) }
    }
}