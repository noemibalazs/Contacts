package com.example.contacts.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contacts.base.BaseViewModel
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import com.example.contacts.utils.USER_RESULTS
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    fun observeViewState(): LiveData<ViewState> = _viewState

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    val errorMessageVisibility = _users.value?.isEmpty()

    fun loadUsers() {
        Single.just(true)
            .doOnSubscribe { setViewState(ViewState.UsersLoading) }
            .doOnSubscribe { Timber.d("Users are loading") }
            .subscribeOn(Schedulers.io())
            .flatMap { userRepository.getUsers(USER_RESULTS) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Timber.e("Error while loading users: ${it.message}") }
            .doOnSuccess { setViewState(ViewState.UsersLoaded) }
            .subscribe(_users::postValue) { setViewState(ViewState.UsersFailed) }
            .addToDisposable()
    }

    private fun setViewState(state: ViewState) {
        _viewState.postValue(state)
    }

    sealed class ViewState {
        object UsersLoading : ViewState()
        object UsersLoaded : ViewState()
        object UsersFailed : ViewState()
    }
}