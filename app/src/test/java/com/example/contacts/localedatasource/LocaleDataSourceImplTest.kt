package com.example.contacts.localedatasource

import com.example.contacts.model.User
import com.example.contacts.room.UsersDAO
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocaleDataSourceImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @MockK
    private lateinit var userDao: UsersDAO

    private lateinit var localeDataSource: LocaleDataSource

    private val user: User = mockk()
    private val error: Exception = mockk()

    @Before
    @Test
    fun setUp() {
        localeDataSource = LocaleDataSourceImpl(userDao)
    }

    @Test
    fun `test observe users and is successful`() {

        every { userDao.observeUsers() } returns Observable.just(listOf(user))

        localeDataSource.observeUsers()
            .test()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { userDao.observeUsers() }
    }

    @Test
    fun `test observe users and throws error`() {
        every { userDao.observeUsers() } returns Observable.error(error)

        localeDataSource.observeUsers()
            .test()
            .assertNotComplete()
            .assertError(error)

        verify(exactly = 1) { userDao.observeUsers() }
    }

    @Test
    fun `test save users and is successful`() {
        every { userDao.saveUsers(listOf(user)) } returns Completable.complete()

        localeDataSource.saveUsers(listOf(user))
            .test()
            .assertNoValues()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { userDao.saveUsers(listOf(user)) }
    }


    @Test
    fun `test save users and throws exception`() {
        every { userDao.saveUsers(listOf(user)) } returns Completable.error(error)

        localeDataSource.saveUsers(listOf(user))
            .test()
            .assertNotComplete()
            .assertError(error)

        verify(exactly = 1) { userDao.saveUsers(listOf(user)) }
    }

    @Test
    fun `test clear users and is successful`() {
        every { userDao.clearUsers() } returns Completable.complete()

        localeDataSource.clearUsers()
            .test()
            .assertNoValues()
            .assertNoErrors()
            .assertComplete()

        verify(exactly = 1) { userDao.clearUsers() }
    }


    @Test
    fun `test clear users and throws exception`() {
        every { userDao.clearUsers() } returns Completable.error(error)

        localeDataSource.clearUsers()
            .test()
            .assertNotComplete()
            .assertError(error)

        verify(exactly = 1) { userDao.clearUsers() }
    }
}