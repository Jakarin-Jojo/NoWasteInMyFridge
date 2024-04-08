package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.UserCreate
import com.develop.nowasteinmyfridge.domain.CreateUserUseCase
import com.develop.nowasteinmyfridge.feature.signup.SignUpViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.develop.nowasteinmyfridge.util.Result
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
class SignUpViewModelTest {

    private lateinit var viewModel: SignUpViewModel
    private val createUserUseCase: CreateUserUseCase = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test creating user success`() = runTest {
        // Given
        val userCreate = mockk<UserCreate>()
        coEvery { createUserUseCase.invoke(userCreate) } returns flowOf(Result.Success(Unit))
        viewModel = SignUpViewModel(createUserUseCase)
        // When
        viewModel.createUser(userCreate)

        // Then
        advanceUntilIdle()
        coVerify(exactly = 1) { createUserUseCase.invoke(userCreate) }
        assertEquals(Result.Success(Unit), viewModel.createUserResult.value)
    }

}