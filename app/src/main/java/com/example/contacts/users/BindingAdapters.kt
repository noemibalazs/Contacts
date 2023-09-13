package com.example.contacts.users

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contacts.R
import com.example.contacts.model.User
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("users")
fun bindUsers(recycleView: RecyclerView, users: List<User>?) {
    (recycleView.adapter as UsersAdapter).submitList(users)
}

@BindingAdapter("viewIsVisible")
fun bindViewVisibility(view: View, isVisible: Boolean) {
    view.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@BindingAdapter("progressBarState")
fun bindProgressBarState(progressBar: CircularProgressIndicator, state: UsersViewModel.ViewState?) {
    progressBar.isVisible = state == UsersViewModel.ViewState.UsersLoading
}

@BindingAdapter("userName")
fun bindUserName(view: AppCompatTextView, user: User) {
    view.text =
        view.context.getString(R.string.label_user_name, user.name.firstName, user.name.lastName)
}

@BindingAdapter("userAddress")
fun bindUserAddress(view: AppCompatTextView, user: User) {
    view.text =
        String.format(
            view.context.getString(R.string.label_user_address), user.location.city,
            user.location.street.name,
            user.location.street.number
        )
}

@BindingAdapter("userAvatar")
fun bindUserAvatar(view: AppCompatImageView, userPicture: String?) {
    Glide.with(view.context)
        .load(userPicture)
        .circleCrop()
        .placeholder(R.drawable.mask)
        .error(R.drawable.mask)
        .into(view)
}