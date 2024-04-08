package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.data.model.Product
import com.develop.nowasteinmyfridge.domain.AddIngredientUseCase
import com.develop.nowasteinmyfridge.domain.GetIngredientByBarcodeUseCase
import com.develop.nowasteinmyfridge.feature.adding.AddingViewModel
import com.develop.nowasteinmyfridge.util.Result
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AddingViewModelTest {

    private lateinit var getIngredientByBarcodeUseCase: GetIngredientByBarcodeUseCase
    private lateinit var addIngredientUseCase: AddIngredientUseCase
    private lateinit var viewModel: AddingViewModel

    @Before
    fun setUp() {
        getIngredientByBarcodeUseCase = mockk()
        addIngredientUseCase = mockk()
        MockKAnnotations.init(this)
        viewModel = AddingViewModel(addIngredientUseCase, getIngredientByBarcodeUseCase)
    }

    @After
    fun tearDown() {
        // Cleanup
        unmockkAll()
    }

    @Test
    fun testAddIngredientSuccess() {
        runTest {
            // Given
            val ingredient = mockk<IngredientCreate>()
            coEvery { addIngredientUseCase.invoke(any()) } just Runs

            // When
            viewModel.addIngredient(ingredient)

            // Then
            advanceUntilIdle()
            coVerify(exactly = 1) { addIngredientUseCase.invoke(any()) }
            assertEquals(Result.Success(Unit), viewModel.addIngredientResult.value)
        }
    }
}