package com.example.contacts.repository

import com.example.contacts.localedatasource.LocaleDataSource
import com.example.contacts.model.RemoteUser
import com.example.contacts.model.User
import com.example.contacts.model.toUser
import com.example.contacts.remotedatasource.RemoteDataSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @MockK
    private lateinit var localeDataSource: LocaleDataSource

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource


    private lateinit var userRepository: UserRepository

    private val remoteUser: RemoteUser = mockk()
    private val user: User = mockk()
    private val error: Exception = mockk()

    @Before
    fun setUp() {
        userRepository = UserRepositoryImpl(
            localeDataSource = localeDataSource,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `test get users and should be successful`() {

        every { remoteDataSource.getRandomUsers(12) } returns Single.just(listOf(remoteUser))
        every { localeDataSource.saveUsers(listOf(user)) } returns Completable.complete()

        mockkStatic(RemoteUser::toUser)
        every { any<RemoteUser>().toUser() } returns user

        userRepository.getUsers(12)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == listOf(user) }

        verify(exactly = 1) { remoteDataSource.getRandomUsers(12) }
        verify(exactly = 1) { localeDataSource.saveUsers(listOf(user)) }
    }

    @Test
    fun `test get users and throws exception`() {

        every { remoteDataSource.getRandomUsers(12) } returns Single.error(error)
        every { localeDataSource.saveUsers(listOf(user)) } returns Completable.complete()

        mockkStatic(RemoteUser::toUser)
        every { any<RemoteUser>().toUser() } returns user

        userRepository.getUsers(12)
            .test()
            .assertNotComplete()
            .assertNoValues()
            .assertError(error)

        verify(exactly = 1) { remoteDataSource.getRandomUsers(12) }
        verify(exactly = 0) { localeDataSource.saveUsers(listOf(user)) }
    }

    @Test
    fun `test get user and should be successful`() {
        val seed = "seed"
        every { remoteDataSource.getUser(seed) } returns Single.just(remoteUser)

        mockkStatic(RemoteUser::toUser)
        every { any<RemoteUser>().toUser() } returns user

        userRepository.getUser(seed)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it == user }

        verify(exactly = 1) { remoteDataSource.getUser(seed) }
    }

    @Test
    fun `test get user and throws exception`() {
        val seed = "seed"
        every { remoteDataSource.getUser(seed) } returns Single.error(error)

        userRepository.getUser(seed)
            .test()
            .assertNotComplete()
            .assertNoValues()
            .assertError(error)

        verify(exactly = 1) { remoteDataSource.getUser(seed) }
    }

    @Test
    fun `test observe users and should be successful`() {
        every { localeDataSource.observeUsers() } returns Observable.just(listOf(user))

        userRepository.observeUsers()
            .test()
            .assertNoErrors()
            .assertComplete()
            .assertValue { it == listOf(user) }

        verify(exactly = 1) { localeDataSource.observeUsers() }
    }

    @Test
    fun `test observe users and throws exception`() {
        every { localeDataSource.observeUsers() } returns Observable.error(error)

        userRepository.observeUsers()
            .test()
            .assertNotComplete()
            .assertNoValues()
            .assertError(error)

        verify(exactly = 1) { localeDataSource.observeUsers() }
    }

    @Test
    fun `test clear user data base and should be successful`() {
        every { localeDataSource.clearUsers() } returns Completable.complete()

        userRepository.clearUserDataBase()
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { localeDataSource.clearUsers() }
    }

    @Test
    fun `test clear user data base and throws exception`() {
        every { localeDataSource.clearUsers() } returns Completable.error(error)

        userRepository.clearUserDataBase()
            .test()
            .assertNotComplete()
            .assertError(error)

        verify(exactly = 1) { localeDataSource.clearUsers() }
    }
}