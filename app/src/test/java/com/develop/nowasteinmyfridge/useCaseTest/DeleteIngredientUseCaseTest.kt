package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.domain.DeleteIngredientUseCase
import com.develop.nowasteinmyfridge.util.Result
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class DeleteIngredientUseCaseTest {

    @Test
    fun `test deleting ingredient`() {
        runTest {
            // Given
            val ingredientId = "123"
            val firestoreRepository = mockk<FirebaseFirestoreRepository>()
            val deleteIngredientUseCase = DeleteIngredientUseCase(firestoreRepository)
            coEvery { firestoreRepository.deleteIngredient(ingredientId) } returns Result.Success(Unit)

            // When
            deleteIngredientUseCase(ingredientId)

            // Then
            coVerify(exactly = 1) { firestoreRepository.deleteIngredient(ingredientId) }
            assertEquals(Result.Success(Unit),firestoreRepository.deleteIngredient(ingredientId))
        }
    }
}