package com.develop.nowasteinmyfridge.feature.adding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.data.model.Product
import com.develop.nowasteinmyfridge.domain.AddIngredientUseCase
import com.develop.nowasteinmyfridge.domain.GetIngredientByBarcodeUseCase
import com.develop.nowasteinmyfridge.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddingViewModel @Inject constructor(
    private val addIngredientUseCase: AddIngredientUseCase,
    private val getIngredientByBarcodeUseCase: GetIngredientByBarcodeUseCase,
) : ViewModel() {
    private val _addIngredientResult = MutableStateFlow<Result<Unit>?>(null)
    val addIngredientResult: StateFlow<Result<Unit>?>
        get() = _addIngredientResult

    private val _brands = MutableStateFlow<String?>(null)
    val brands: StateFlow<String?>
        get() = _brands

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String>
        get() = _imageUrl
    private val _isFoundProduct = MutableStateFlow<Int?>(null)
    val isFoundProduct: StateFlow<Int?>
        get() = _isFoundProduct
    private val _getIngredientByBarcodeResult = MutableStateFlow<Result<Unit>?>(null)
    val getIngredientByBarcodeResult: StateFlow<Result<Unit>?>
        get() = _getIngredientByBarcodeResult

    fun getIngredientByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _getIngredientByBarcodeResult.value = Result.Loading
                val result = getIngredientByBarcodeUseCase(barcode)
                Log.d("AddingViewModel", "Result: $result")
                val brands = parseBrandsFromResponse(result)
                _brands.value = brands
                val imageUrl = result.product.image_url
                _imageUrl.value = imageUrl
                _isFoundProduct.value = result.status
                _getIngredientByBarcodeResult.value = Result.Success(Unit)
                Log.d("AddingViewModel", "Brands: $brands")
            } catch (e: Exception) {
                Log.e("AddingViewModel", "Error getting ingredient by barcode", e)
                _getIngredientByBarcodeResult.value = Result.Error(e)
            }
        }
    }

    private fun parseBrandsFromResponse(product: Product): String {
        val brands = product.product.brands
        return brands.split(",")[0]
    }

    fun addIngredient(ingredient: IngredientCreate) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _addIngredientResult.value = Result.Loading
                addIngredientUseCase.invoke(ingredient = ingredient)
                _addIngredientResult.value = Result.Success(Unit)
            } catch (e: Exception) {
                _addIngredientResult.value = Result.Error(e)
                Log.e("addIngredient", "Unable to addIngredient", e)
            }
        }
    }

    fun checkAndAutoFillEfdDate(
        name: String?,
        mfgDate: Calendar,
        isInFridge: Boolean,
    ): Calendar? {
        val recommendedEfdDate = Calendar.getInstance()

        if (name.isNullOrEmpty()) {
            Log.d("checkAndAutoFillEfdDate", "Name is empty or null.")
            return null
        }

        when (name.lowercase(Locale.getDefault())) {
            in listOf("pork", "cow", "sheep", "goat", "beef") -> {
                recommendedEfdDate.timeInMillis = mfgDate.timeInMillis
                recommendedEfdDate.add(Calendar.DAY_OF_YEAR, if (isInFridge) 360 else 5)
            }

            in listOf("chicken", "duck") -> {
                recommendedEfdDate.timeInMillis = mfgDate.timeInMillis
                recommendedEfdDate.add(Calendar.DAY_OF_YEAR, if (isInFridge) 360 else 2)
            }

            in listOf("shrimp", "shellfish", "crab", "squid", "fish") -> {
                recommendedEfdDate.timeInMillis = mfgDate.timeInMillis
                recommendedEfdDate.add(Calendar.DAY_OF_YEAR, if (isInFridge) 72 else 2)
            }

            "egg" -> {
                recommendedEfdDate.timeInMillis = mfgDate.timeInMillis
                recommendedEfdDate.add(Calendar.DAY_OF_YEAR, if (isInFridge) 360 else 35)
            }

            in listOf(
                "kale", "coriander", "ginger", "galangal", "carrots", "onions", "taro",
                "eggplant", "zucchini", "fresh chilies", "oranges", "pineapples", "grapes",
                "sapodilla", "guava", "mango",
            ) -> {
                if (isInFridge) {
                    Log.d("checkAndAutoFillEfdDate", "Do not freeze this ingredient: $name")
                    return null
                } else {
                    recommendedEfdDate.timeInMillis = mfgDate.timeInMillis
                    when (name.lowercase(Locale.getDefault())) {
                        in listOf(
                            "oranges",
                            "pineapples"
                        ) -> recommendedEfdDate.add(Calendar.DAY_OF_YEAR, 2)

                        in listOf(
                            "grapes",
                            "sapodilla",
                            "guava",
                            "mango"
                        ) -> recommendedEfdDate.add(
                            Calendar.DAY_OF_YEAR, 5
                        )

                        else -> recommendedEfdDate.add(Calendar.DAY_OF_YEAR, 10)
                    }
                }
            }

            else -> {
                Log.d("checkAndAutoFillEfdDate", "No expiration date suggested for name: $name")
                return null
            }
        }

        Log.d("checkAndAutoFillEfdDate", "Recommended Efd Date: $recommendedEfdDate")
        return recommendedEfdDate
    }
}