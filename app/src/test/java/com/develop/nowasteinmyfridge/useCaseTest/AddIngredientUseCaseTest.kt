package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.domain.AddIngredientUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

class AddIngredientUseCaseTest {

    @Test
    fun `test adding ingredient`() = runTest {
        // Create a mock instance of FirebaseFirestoreRepository
        val firestoreRepository = mockk<FirebaseFirestoreRepository>()

        // Create an instance of AddIngredientUseCase with the mocked repository
        val addIngredientUseCase = AddIngredientUseCase(firestoreRepository)

        // Mock the behavior of the repository method using coEvery
        val ingredient = IngredientCreate(image = "")
        coEvery { firestoreRepository.addIngredient(ingredient) } returns Unit

        // Invoke the use case
        addIngredientUseCase(ingredient)

        // Verify that the repository method was called with the correct parameter
        coVerify(exactly = 1) { firestoreRepository.addIngredient(ingredient) }
    }
}