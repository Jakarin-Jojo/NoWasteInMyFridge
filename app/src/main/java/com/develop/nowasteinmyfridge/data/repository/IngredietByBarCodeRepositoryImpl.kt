package com.develop.nowasteinmyfridge.data.repository

import android.util.Log
import com.develop.nowasteinmyfridge.data.api.OpenFoodFactsApi
import com.develop.nowasteinmyfridge.data.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class IngredientByBarcodeRepositoryImpl @Inject constructor() : IngredientByBarCodeRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://world.openfoodfacts.org/api/v0/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val openFoodFactsApi = retrofit.create(OpenFoodFactsApi::class.java)

    override suspend fun getIngredientByBarcode(barcode: String): Product {
        try {
            val response = openFoodFactsApi.fetchProductByCode(barcode)
            Log.e("API_Response", "Response code: ${response.code()}, Message: ${response.message()}, URL: ${response.raw().request.url}, res: $response")
            Log.e("API_Response", "Response body: ${response.body()?.toString()}")
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    // Return the product
                    return responseBody
                } else {
                    Log.e("API_Response", "Response body is null")
                }
            } else {
                Log.e("API_Response", "Unsuccessful response: ${response.code()}, Message: ${response.message()}")
            }
        } catch (e: IOException) {
            Log.e("IngredientByBarcodeRepo", "Network error: ${e.message}", e)
        }
        Log.d("api call", "no response")
        return Product()
    }

}
