package com.example.contacts.search

import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("progressBarState")
fun bindProgressBarState(
    progressBar: CircularProgressIndicator,
    isVisible: Boolean?
) {
    progressBar.isVisible = isVisible ?: false
}