package com.develop.nowasteinmyfridge.feature.inventory

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.domain.DeleteIngredientUseCase
import com.develop.nowasteinmyfridge.domain.GetIngredientsUseCase
import com.develop.nowasteinmyfridge.domain.UpdateIngredientQuantityUseCase
import com.develop.nowasteinmyfridge.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getIngredientsUseCase: GetIngredientsUseCase,
    private val deleteIngredientUseCase: DeleteIngredientUseCase,
    private val updateIngredientQuantityUseCase: UpdateIngredientQuantityUseCase,
) : ViewModel() {
    private val _ingredientsState = mutableStateOf<List<Ingredient>>(emptyList())
    val ingredientsState: State<List<Ingredient>>
        get() = _ingredientsState
    private val _updateIngredientQuantityState = MutableStateFlow<Result<Unit>?>(null)
    val updateIngredientQuantityState: StateFlow<Result<Unit>?>
        get() = _updateIngredientQuantityState
    private val _deleteIngredientState = MutableStateFlow<Result<Unit>?>(null)
    val deleteIngredientState : StateFlow<Result<Unit>?>
        get() = _deleteIngredientState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getIngredients()
        }
    }

    private suspend fun getIngredients() {
        try {
            _ingredientsState.value = getIngredientsUseCase.invoke()
        } catch (e: Exception) {
            Log.e("getIngredients", "Unable to getIngredients", e)
        }

    }

    fun deleteIngredient(ingredientID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _deleteIngredientState.value = Result.Loading
                deleteIngredientUseCase.invoke(ingredientID)
                getIngredients()
                _deleteIngredientState.value = Result.Success(Unit)
            } catch (e: Exception) {
                Log.e("deleteIngredient", "Unable to delete Ingredient", e)
                _deleteIngredientState.value = Result.Error(e)
            }
        }
    }

    fun updateIngredientQuantity(ingredientID: String, newQuantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _updateIngredientQuantityState.value = Result.Loading
                updateIngredientQuantityUseCase.invoke(ingredientID, newQuantity)
                getIngredients()
                _updateIngredientQuantityState.value = Result.Success(Unit)
            } catch (e: Exception) {
                Log.e("updateIngredientQuantity", "Unable to update ingredient quantity", e)
                _updateIngredientQuantityState.value = Result.Error(e)
            }
        }
    }
}
