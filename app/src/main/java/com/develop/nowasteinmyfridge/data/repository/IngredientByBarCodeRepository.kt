package com.develop.nowasteinmyfridge.data.repository

import com.develop.nowasteinmyfridge.data.model.Product

interface IngredientByBarCodeRepository {
    suspend fun getIngredientByBarcode(barcode: String): Product
}
