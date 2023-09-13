package com.example.contacts.userdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.contacts.base.BaseViewModel
import com.example.contacts.model.User
import com.example.contacts.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    fun observeViewState(): LiveData<ViewState> = _viewState

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun loadUserDetails(seed: String) =
        Single.just(true)
            .doOnSubscribe { setViewState(ViewState.UserDetailsLoading) }
            .doOnSubscribe { Timber.d("Loading user details") }
            .subscribeOn(Schedulers.io())
            .flatMap {
                userRepository.getUser(seed)
            }
            .map { _user.postValue(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { Timber.e("Error while loading user details.") }
            .doOnTerminate { Timber.d("User details loaded.") }
            .subscribe({ setViewState(ViewState.UserDetailsLoaded) }) { setViewState(ViewState.UserDetailsFailed) }
            .addToDisposable()

    private fun setViewState(state: ViewState) {
        _viewState.postValue(state)
    }

    sealed class ViewState {
        object UserDetailsLoading : ViewState()
        object UserDetailsLoaded : ViewState()
        object UserDetailsFailed : ViewState()
    }
}