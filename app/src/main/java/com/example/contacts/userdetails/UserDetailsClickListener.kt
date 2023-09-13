package com.example.contacts.userdetails

interface UserDetailsClickListener {

    fun onPhoneClicked()

    fun onEmailClicked(email: String)
}