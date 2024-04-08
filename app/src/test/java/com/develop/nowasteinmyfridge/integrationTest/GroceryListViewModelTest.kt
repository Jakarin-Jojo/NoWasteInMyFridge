package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.GroceryList
import com.develop.nowasteinmyfridge.data.model.GroceryListCreate
import com.develop.nowasteinmyfridge.domain.AddGroceryListUseCase
import com.develop.nowasteinmyfridge.domain.ClearGroceryListUseCase
import com.develop.nowasteinmyfridge.domain.GetGroceryListUseCase
import com.develop.nowasteinmyfridge.feature.grocerylist.GroceryListViewModel
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
class GroceryListViewModelTest {

    private lateinit var viewModel: GroceryListViewModel
    private val addGroceryListUseCase: AddGroceryListUseCase = mockk()
    private val getGroceryListUseCase: GetGroceryListUseCase = mockk()
    private val clearGroceryListUseCase: ClearGroceryListUseCase = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getting grocery list success`() = runTest {
        // Given
        val groceryList = listOf(GroceryList()) // Your grocery list data
        coEvery { getGroceryListUseCase.invoke() } returns listOf(GroceryList())
        viewModel = GroceryListViewModel(
            addGroceryListUseCase,
            getGroceryListUseCase,
            clearGroceryListUseCase
        )

        // Then
        advanceUntilIdle()
        coVerify(exactly = 1) { getGroceryListUseCase.invoke() }
        assertEquals(groceryList, viewModel.groceryListState.value)
    }

    @Test
    fun `test adding grocery list success`() = runTest {
        // Given
        val groceryListCreate = mockk<GroceryListCreate>()
        coEvery { addGroceryListUseCase.invoke(groceryListCreate) } just Runs
        coEvery { getGroceryListUseCase.invoke() } returns listOf(GroceryList())
        viewModel = GroceryListViewModel(
            addGroceryListUseCase,
            getGroceryListUseCase,
            clearGroceryListUseCase
        )

        // When
        viewModel.addGroceryList(groceryListCreate)

        // Then
        advanceUntilIdle()
        coVerify(exactly = 1) { addGroceryListUseCase.invoke(groceryListCreate) }
        assertEquals(Result.Success(Unit), viewModel.addGroceryListResult.value)
    }

    @Test
    fun `test clearing grocery list success`() = runTest {
        // Given
        coEvery { clearGroceryListUseCase.invoke() } just Runs
        coEvery { getGroceryListUseCase.invoke() } returns emptyList<GroceryList>()
        viewModel = GroceryListViewModel(
            addGroceryListUseCase,
            getGroceryListUseCase,
            clearGroceryListUseCase
        )

        // When
        viewModel.clearGroceryList()

        // Then
        advanceUntilIdle()
        coVerify(exactly = 1) { clearGroceryListUseCase.invoke() }
        assertEquals(emptyList<GroceryList>(), viewModel.groceryListState.value)
    }
}