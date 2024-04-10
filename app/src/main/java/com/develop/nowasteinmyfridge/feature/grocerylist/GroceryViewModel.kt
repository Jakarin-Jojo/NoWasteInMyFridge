package com.develop.nowasteinmyfridge.feature.grocerylist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.nowasteinmyfridge.data.model.GroceryList
import com.develop.nowasteinmyfridge.data.model.GroceryListCreate
import com.develop.nowasteinmyfridge.domain.AddGroceryListUseCase
import com.develop.nowasteinmyfridge.domain.ClearGroceryListUseCase
import com.develop.nowasteinmyfridge.domain.GetGroceryListUseCase
import com.develop.nowasteinmyfridge.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryListViewModel @Inject constructor(
    private val addGroceryListUseCase: AddGroceryListUseCase,
    private val getGroceryListUseCase: GetGroceryListUseCase,
    private val clearGroceryListUseCase: ClearGroceryListUseCase,
) : ViewModel() {

    private val _addGroceryListResult = mutableStateOf<Result<Unit>?>(null)
    val addGroceryListResult: State<Result<Unit>?> = _addGroceryListResult

    private val _groceryListState = mutableStateOf<List<GroceryList>>(emptyList())
    val groceryListState: State<List<GroceryList>> = _groceryListState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getGroceryList()
        }
    }

    private suspend fun getGroceryList() {
        _groceryListState.value = getGroceryListUseCase.invoke()
    }

    fun addGroceryList(groceryList: GroceryListCreate) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _addGroceryListResult.value = Result.Loading
                addGroceryListUseCase.invoke(groceryList)
                _addGroceryListResult.value = Result.Success(Unit)
                getGroceryList()
            } catch (e: Exception) {
                _addGroceryListResult.value = Result.Error(e)
                Log.e("GroceryListViewModel", "Error adding grocery list: $e")
            }
        }
    }
    fun clearGroceryList() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                clearGroceryListUseCase.invoke()
                getGroceryList()
            } catch (e: Exception) {
                Log.e("GroceryListViewModel", "Error clearing grocery list: $e")
            }
        }
    }
}
