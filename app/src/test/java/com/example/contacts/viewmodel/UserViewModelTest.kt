package com.example.contacts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.contacts.RxJavaSchedulerTrampolineRule
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import com.example.contacts.users.UsersViewModel
import com.example.contacts.utils.USER_RESULTS
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RxJavaSchedulerTrampolineRule()

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var viewStateObserver: Observer<UsersViewModel.ViewState>

    @MockK
    private lateinit var usersObserver: Observer<List<User>>

    private val usersCaptor = mutableListOf<List<User>>()
    private val stateCaptor = mutableListOf<UsersViewModel.ViewState>()

    private lateinit var viewModel: UsersViewModel

    private val user: User = mockk()
    private val error: Exception = mockk()

    @Before
    fun setUp() {
        viewModel = UsersViewModel(userRepository)

        usersCaptor.clear()
        stateCaptor.clear()

        every { usersObserver.onChanged(capture(usersCaptor)) } just runs
        every { viewStateObserver.onChanged(capture(stateCaptor)) } just runs

        viewModel.observeViewState().observeForever(viewStateObserver)
        viewModel.users.observeForever(usersObserver)
    }

    @Test
    fun `test load users and should be successful`() {
        every { userRepository.getUsers(USER_RESULTS) } returns Single.just(listOf(user))

        viewModel.loadUsers()

        verify(exactly = 1) { userRepository.getUsers(USER_RESULTS) }
        verify(exactly = 1) { viewStateObserver.onChanged(UsersViewModel.ViewState.UsersLoading) }
        verify(exactly = 1) { usersObserver.onChanged(listOf(user)) }
        verify(exactly = 1) { viewStateObserver.onChanged(UsersViewModel.ViewState.UsersLoaded) }
    }

    @Test
    fun `test load users and throws exception`() {
        every { userRepository.getUsers(USER_RESULTS) } returns Single.error(error)

        viewModel.loadUsers()

        verify(exactly = 1) { userRepository.getUsers(USER_RESULTS) }
        verify(exactly = 1) { viewStateObserver.onChanged(UsersViewModel.ViewState.UsersLoading) }
        verify(exactly = 0) { usersObserver.onChanged(any()) }
        verify(exactly = 1) { viewStateObserver.onChanged(UsersViewModel.ViewState.UsersFailed) }
    }
}