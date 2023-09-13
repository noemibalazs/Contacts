package com.example.contacts.utils

import android.util.Patterns
import javax.inject.Inject

class InputValidationImpl @Inject constructor() : InputValidation {

    override fun isValidEmailAddress(email: String): Boolean =
        email.matches(Patterns.EMAIL_ADDRESS.toRegex())
}