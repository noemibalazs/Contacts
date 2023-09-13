package com.example.contacts.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.contacts.RxJavaSchedulerTrampolineRule
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import com.example.contacts.search.SearchViewModel
import com.example.contacts.utils.InputValidation
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class SearchViewModelTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RxJavaSchedulerTrampolineRule()

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var inputValidation: InputValidation

    @MockK
    private lateinit var viewStateObserver: Observer<SearchViewModel.ViewState>

    @MockK
    private lateinit var usersObserver: Observer<List<User>>

    @MockK
    private lateinit var progressVisibilityObserver: Observer<Boolean>

    @MockK
    private lateinit var emailAddressObserver: Observer<Boolean>

    private val usersCaptor = mutableListOf<List<User>>()
    private val stateCaptor = mutableListOf<SearchViewModel.ViewState>()
    private val progressCaptor = mutableListOf<Boolean>()
    private val emailAddressCaptor = mutableListOf<Boolean>()

    private lateinit var viewModel: SearchViewModel

    private val testScheduler = TestScheduler()

    private val user: User = mockk()
    private val error: Exception = mockk()

    @Before
    fun setUp() {
        viewModel = SearchViewModel(
            userRepository = userRepository,
            inputValidation = inputValidation
        )

        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }

        usersCaptor.clear()
        stateCaptor.clear()
        progressCaptor.clear()
        emailAddressCaptor.clear()

        every { usersObserver.onChanged(capture(usersCaptor)) } just runs
        every { viewStateObserver.onChanged(capture(stateCaptor)) } just runs
        every { progressVisibilityObserver.onChanged(capture(progressCaptor)) } just runs
        every { emailAddressObserver.onChanged(capture(emailAddressCaptor)) } just runs

        viewModel.observeViewState().observeForever(viewStateObserver)
        viewModel.progressVisibility.observeForever(progressVisibilityObserver)
        viewModel.isValidEmailAddress.observeForever(emailAddressObserver)
        viewModel.users.observeForever(usersObserver)

        viewModel.initField()
    }

    @Test
    fun `test init field and should be successful`() {
        val email = "jane.doe@gmail.com"
        viewModel.searchedTermUpdated(email)

        every { inputValidation.isValidEmailAddress(email) } returns true

        every { userRepository.observeUsers() } returns Observable.just(listOf(user))
        every { user.email } returns email

        testScheduler.advanceTimeBy(360L, TimeUnit.MILLISECONDS)

        viewModel.initField()

        verify(exactly = 1) { emailAddressObserver.onChanged(true) }
        verify(exactly = 1) { progressVisibilityObserver.onChanged(true) }
        verify(exactly = 1) { usersObserver.onChanged(listOf(user)) }
        verify(exactly = 1) { progressVisibilityObserver.onChanged(false) }
        verify(exactly = 1) { viewStateObserver.onChanged(SearchViewModel.ViewState.SearchingUsersFinished) }
    }


    @Test
    fun `test init field and should throws exception`() {
        val email = "jane.doe@gmail.com"
        viewModel.searchedTermUpdated(email)

        every { inputValidation.isValidEmailAddress(email) } returns true

        every { userRepository.observeUsers() } returns Observable.error(error)

        testScheduler.advanceTimeBy(360L, TimeUnit.MILLISECONDS)

        viewModel.initField()

        verify(exactly = 1) { emailAddressObserver.onChanged(true) }
        verify(exactly = 1) { progressVisibilityObserver.onChanged(true) }
        verify(exactly = 0) { usersObserver.onChanged(any()) }
        verify(exactly = 1) { progressVisibilityObserver.onChanged(false) }
        verify(exactly = 1) { viewStateObserver.onChanged(SearchViewModel.ViewState.SearchingUsersFailed) }
    }
}