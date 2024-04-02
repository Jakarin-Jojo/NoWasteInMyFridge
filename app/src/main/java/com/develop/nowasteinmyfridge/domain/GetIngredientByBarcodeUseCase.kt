package com.develop.nowasteinmyfridge.domain

import com.develop.nowasteinmyfridge.data.model.Product
import com.develop.nowasteinmyfridge.data.repository.IngredientByBarCodeRepository
import javax.inject.Inject

class GetIngredientByBarcodeUseCase @Inject constructor(
    private val providesGetBarCodeRepository: IngredientByBarCodeRepository
) {
    suspend operator fun invoke(ingredientQrcodeID: String): Product {
        return providesGetBarCodeRepository.getIngredientByBarcode(ingredientQrcodeID)
    }
}

