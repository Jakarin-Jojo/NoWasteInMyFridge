package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.model.UserCreate
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepositoryImpl
import com.develop.nowasteinmyfridge.domain.CreateUserUseCase
import com.develop.nowasteinmyfridge.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CreateUserUseCaseTest {
    @Test
    fun `test create user success`() = runTest {
        // Given
        val userCreate = mockk<UserCreate>()
        val firebaseFirestoreRepositoryImpl = mockk<FirebaseFirestoreRepositoryImpl>()
        val createUserUseCase = CreateUserUseCase(firebaseFirestoreRepositoryImpl)
        val expectedResult = Result.Success(Unit)
        coEvery { firebaseFirestoreRepositoryImpl.createUser(userCreate) } returns flowOf(expectedResult)

        // When
        val result = createUserUseCase(userCreate).single()

        // Then
        coVerify(exactly = 1) { firebaseFirestoreRepositoryImpl.createUser(userCreate) }
        assertEquals(expectedResult, result)
    }
}