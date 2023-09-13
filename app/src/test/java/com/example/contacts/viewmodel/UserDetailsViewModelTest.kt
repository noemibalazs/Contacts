package com.example.contacts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.contacts.RxJavaSchedulerTrampolineRule
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import com.example.contacts.userdetails.UserDetailsViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDetailsViewModelTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RxJavaSchedulerTrampolineRule()

    @MockK
    private lateinit var userRepository: UserRepository


    @MockK
    private lateinit var viewStateObserver: Observer<UserDetailsViewModel.ViewState>

    @MockK
    private lateinit var userObserver: Observer<User>

    private val userCaptor = mutableListOf<User>()
    private val stateCaptor = mutableListOf<UserDetailsViewModel.ViewState>()

    private lateinit var viewModel: UserDetailsViewModel

    private val user: User = mockk()
    private val error: Exception = mockk()

    @Before
    fun setUp() {
        viewModel = UserDetailsViewModel(userRepository)

        userCaptor.clear()
        stateCaptor.clear()

        every { userObserver.onChanged(capture(userCaptor)) } just runs
        every { viewStateObserver.onChanged(capture(stateCaptor)) } just runs

        viewModel.observeViewState().observeForever(viewStateObserver)
        viewModel.user.observeForever(userObserver)
    }

    @Test
    fun `test load user details and should be successful`() {
        val seed = "seed"
        every { userRepository.getUser(seed) } returns Single.just(user)

        viewModel.loadUserDetails(seed)

        verify(exactly = 1) { userRepository.getUser(seed) }
        verify(exactly = 1) { viewStateObserver.onChanged(UserDetailsViewModel.ViewState.UserDetailsLoading) }
        verify(exactly = 1) { userObserver.onChanged(user) }
        verify(exactly = 1) { viewStateObserver.onChanged(UserDetailsViewModel.ViewState.UserDetailsLoaded) }
    }


    @Test
    fun `test load user details and throws exception`() {
        val seed = "seed"
        every { userRepository.getUser(seed) } returns Single.error(error)

        viewModel.loadUserDetails(seed)

        verify(exactly = 1) { userRepository.getUser(seed) }
        verify(exactly = 1) { viewStateObserver.onChanged(UserDetailsViewModel.ViewState.UserDetailsLoading) }
        verify(exactly = 0) { userObserver.onChanged(any()) }
        verify(exactly = 1) { viewStateObserver.onChanged(UserDetailsViewModel.ViewState.UserDetailsFailed) }
    }
}