package com.example.contacts.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contacts.base.BaseViewModel
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import com.example.contacts.utils.InputValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val inputValidation: InputValidation
) :
    BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    fun observeViewState(): LiveData<ViewState> = _viewState

    private val searchedEmailAddress = BehaviorSubject.create<String>()
    fun searchedTermUpdated(email: String) = searchedEmailAddress.onNext(email)

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _progressVisibility = MutableLiveData<Boolean>()
    val progressVisibility: LiveData<Boolean> = _progressVisibility

    private val _isValidEmailAddress = MutableLiveData<Boolean>()
    val isValidEmailAddress: LiveData<Boolean> = _isValidEmailAddress

    fun initField() {
        searchedEmailAddress
            .doOnNext() { Timber.d("The searched term is: $it") }
            .debounce(360L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .subscribeOn(Schedulers.io())
            .map { email ->
                _isValidEmailAddress.postValue(inputValidation.isValidEmailAddress(email))
                email
            }
            .flatMap { emailAddress ->
                _progressVisibility.postValue(true)
                userRepository.observeUsers().defaultIfEmpty(emptyList())
                    .map { users -> emailAddress to users }
            }
            .map { (email, users) ->
                val filteredUsers = users.filter { user -> user.email == email }
                _users.postValue(filteredUsers)
                _progressVisibility.postValue(false)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError {
                _progressVisibility.postValue(false)
                Timber.e("Error while searching for a user.")
            }
            .subscribe({ setViewState(ViewState.SearchingUsersFinished) }) { setViewState(ViewState.SearchingUsersFailed) }
            .addToDisposable()
    }

    private fun setViewState(state: ViewState) = _viewState.postValue(state)

    fun setEmailAddressState() = _isValidEmailAddress.postValue(true)

    sealed class ViewState {
        object SearchingUsersFinished : ViewState()
        object SearchingUsersFailed : ViewState()
    }
}