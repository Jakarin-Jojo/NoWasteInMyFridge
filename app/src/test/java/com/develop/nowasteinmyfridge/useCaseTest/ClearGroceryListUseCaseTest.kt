package com.develop.nowasteinmyfridge.useCaseTest

import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import com.develop.nowasteinmyfridge.domain.ClearGroceryListUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ClearGroceryListUseCaseTest {

    @Test
    fun `test clearing grocery list`() {
        runTest {
        // Given
        val firestoreRepository = mockk<FirebaseFirestoreRepository>()
        val clearGroceryListUseCase = ClearGroceryListUseCase(firestoreRepository)
        coEvery { firestoreRepository.clearGroceryList() } just Runs

        // When
            clearGroceryListUseCase()

        // Then
        coVerify(exactly = 1) { firestoreRepository.clearGroceryList() }
    }
}
}