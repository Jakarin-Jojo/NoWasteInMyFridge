package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.domain.GetIngredientsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetIngredientsUseCaseTest {

    @Test
    fun `test get ingredients success`() = runTest {
        // Given
        val ingredients = listOf(
            Ingredient(id = "1", name = "Ingredient 1"),
            Ingredient(id = "2", name = "Ingredient 2")
        )
        val firestoreRepository = mockk<FirebaseFirestoreRepository>()
        val getIngredientsUseCase = GetIngredientsUseCase(firestoreRepository)
        coEvery { firestoreRepository.getIngredient() } returns ingredients

        // When
        val result = getIngredientsUseCase()

        // Then
        coVerify(exactly = 1) { firestoreRepository.getIngredient() }
        assertEquals(ingredients, result)
    }

    @Test
    fun `test get ingredients empty list`() = runTest {
        // Given
        val ingredients = emptyList<Ingredient>()
        val firestoreRepository = mockk<FirebaseFirestoreRepository>()
        val getIngredientsUseCase = GetIngredientsUseCase(firestoreRepository)
        coEvery { firestoreRepository.getIngredient() } returns ingredients

        // When
        val result = getIngredientsUseCase()

        // Then
        coVerify(exactly = 1) { firestoreRepository.getIngredient() }
        assertEquals(ingredients, result)
    }
}