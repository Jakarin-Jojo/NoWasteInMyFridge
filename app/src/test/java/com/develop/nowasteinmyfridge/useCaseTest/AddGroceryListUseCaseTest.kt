package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.model.GroceryListCreate
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.domain.AddGroceryListUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddGroceryListUseCaseTest {

    @Test
    fun `test adding grocery list`() {
        runTest {
            // Given
            val groceryListCreate = mockk<GroceryListCreate>()
            val firestoreRepository = mockk<FirebaseFirestoreRepository>()
            val addGroceryListUseCase = AddGroceryListUseCase(firestoreRepository)
            coEvery { firestoreRepository.addGroceryList(groceryListCreate) } just Runs


            // When
            addGroceryListUseCase(groceryListCreate)

            // Then
            coVerify(exactly = 1) { firestoreRepository.addGroceryList(groceryListCreate) }
        }
    }
}