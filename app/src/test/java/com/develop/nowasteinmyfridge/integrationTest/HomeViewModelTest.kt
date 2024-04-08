package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.RecipeSearchResponse
import com.develop.nowasteinmyfridge.domain.GetRecipeUseCase
import com.develop.nowasteinmyfridge.feature.home.HomeViewModel
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

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Mock dependencies
    private val getRecipeUseCase: GetRecipeUseCase = mockk()

    // Subject under test
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        // Mockk setup
        MockKAnnotations.init(this)
        // Initialize the ViewModel with mocked dependencies
        viewModel = HomeViewModel(getRecipeUseCase)
    }

    @After
    fun tearDown() {
        // Cleanup
        unmockkAll()
    }

    @Test
    fun `test searchRecipes success`() {
        runTest {
            // Mock data
            val query = "chicken"
            val response = RecipeSearchResponse()

            // Stub the behavior of the use case
            coEvery { getRecipeUseCase(query) } returns response

            // Perform action
            viewModel.searchRecipes(query)

            // Verify that the correct value is set
            advanceUntilIdle()
            coVerify(exactly = 1) { getRecipeUseCase(query) }
            assertEquals(response, viewModel.recipesState.value)
        }
    }
}