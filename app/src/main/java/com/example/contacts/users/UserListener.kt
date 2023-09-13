package com.example.contacts.users

import com.example.contacts.model.User

interface UserListener {

    fun onUserClicked(user: User)
}