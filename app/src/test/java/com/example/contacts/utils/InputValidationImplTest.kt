package com.example.contacts.utils

import io.mockk.junit4.MockKRule
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class InputValidationImplTest {

    @get:Rule
    val mockKRule = MockKRule(this)

    private lateinit var inputValidation: InputValidation

    @Before
    fun setUp() {
        inputValidation = InputValidationImpl()
    }

    @Test(expected = NullPointerException::class)
    fun `test email address should be valid`() {
        val emailAddress = "john.doe@gmail.com"

        val result = inputValidation.isValidEmailAddress(emailAddress)

        assertEquals(result, true)
    }

    @Test(expected = NullPointerException::class)
    fun `test email address should be invalid`() {
        val emailAddress = "john-!doe@gmail.com"

        val result = inputValidation.isValidEmailAddress(emailAddress)

        assertEquals(result, false)
    }
}