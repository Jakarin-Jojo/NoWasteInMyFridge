package com.develop.nowasteinmyfridge.integrationTest

import androidx.lifecycle.Observer
import com.develop.nowasteinmyfridge.domain.LoginUserUseCase
import com.develop.nowasteinmyfridge.feature.login.LoginViewModel
import com.develop.nowasteinmyfridge.feature.login.SignInState
import com.develop.nowasteinmyfridge.util.Resource
import com.google.firebase.auth.AuthResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    // Mock dependencies
    private val loginUserUseCase: LoginUserUseCase = mockk()

    // Subject under test
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        // Initialize MockKAnnotations
        MockKAnnotations.init(this)
        // Initialize the ViewModel with mocked dependencies
        viewModel = LoginViewModel(loginUserUseCase)
    }

    @After
    fun tearDown() {
        // Cleanup
        unmockkAll()
    }

    @Test
    fun `loginUser should emit SignInState with isSuccess when loginUserUseCase succeeds`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password"
        val authResult = mockk<AuthResult>()
        coEvery { loginUserUseCase.invoke(email, password) } returns flowOf(Resource.Success(authResult))

        // Act
        viewModel.loginUser(email, password)

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Assert
        val signInState = viewModel.signInState.first()

        assertTrue(signInState.isSuccess == "Sign In Success")
    }

}