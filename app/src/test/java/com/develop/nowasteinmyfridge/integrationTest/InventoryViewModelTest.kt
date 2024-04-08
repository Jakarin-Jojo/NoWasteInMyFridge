package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.domain.DeleteIngredientUseCase
import com.develop.nowasteinmyfridge.domain.GetIngredientsUseCase
import com.develop.nowasteinmyfridge.feature.inventory.InventoryViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class InventoryViewModelTest {

    // Mock dependencies
    private val getIngredientsUseCase: GetIngredientsUseCase = mockk()
    private val deleteIngredientUseCase: DeleteIngredientUseCase = mockk()

    // Subject under test
    private lateinit var viewModel: InventoryViewModel

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        // Cleanup
        unmockkAll()
    }

    @Test
    fun testGetIngredientsSuccess() = runTest {
        val ingredients = listOf(Ingredient())
        coEvery { getIngredientsUseCase.invoke() } returns ingredients
        viewModel = InventoryViewModel(getIngredientsUseCase, deleteIngredientUseCase)

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Verify that the correct value is set
        coVerify(exactly = 1) { getIngredientsUseCase.invoke() }
        assertEquals(ingredients, viewModel.ingredientsState.value)
    }

    @Test
    fun testDeleteIngredientSuccess() = runTest {
        val ingredients = listOf(Ingredient())
        coEvery { getIngredientsUseCase.invoke() } returns ingredients
        coEvery { deleteIngredientUseCase.invoke(any()) } just runs
        viewModel = InventoryViewModel(getIngredientsUseCase, deleteIngredientUseCase)

        // Perform action
        viewModel.deleteIngredient("ingredientID")

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Verify that the correct value is set after deletion
        coVerify(exactly = 1) { deleteIngredientUseCase.invoke(any()) }
    }
}