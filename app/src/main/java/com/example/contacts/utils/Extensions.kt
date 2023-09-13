package com.example.contacts.utils

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(text: String) = Toast.makeText(this.context, text, Toast.LENGTH_LONG).show()
