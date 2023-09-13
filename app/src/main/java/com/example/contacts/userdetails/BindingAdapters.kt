package com.example.contacts.userdetails

import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.contacts.R

@BindingAdapter("firstName", "lastName")
fun bindUserName(view: TextView, firstName: String?, lastName: String?) {
    view.text = view.context.getString(R.string.label_user_name, firstName, lastName)
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