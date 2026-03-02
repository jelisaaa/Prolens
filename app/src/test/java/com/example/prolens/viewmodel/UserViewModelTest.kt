package com.example.prolens.viewmodel

import com.example.prolens.repository.UserRepoImpl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class UserViewModelTest {

    @Test
    fun login_success_test() {
        // Mocking UserRepoImpl
        val repo = mock<UserRepoImpl>()
        val viewModel = UserViewModel(repo)

        // Defining the behavior for repo.login
        doAnswer { invocation ->
            val callback = invocation.getArgument<(Boolean, String) -> Unit>(2)
            callback(true, "Login success")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any())

        var successResult = false
        var messageResult = ""

        // Calling the method to test
        viewModel.login("test@gmail.com", "123456") { success, msg ->
            successResult = success
            messageResult = msg
        }

        // Verifying results
        assertTrue(successResult)
        assertEquals("Login success", messageResult)

        // Verifying that repo.login was called with correct parameters
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any())
    }
}
