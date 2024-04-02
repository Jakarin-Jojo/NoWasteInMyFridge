package com.develop.nowasteinmyfridge.data.api

import com.develop.nowasteinmyfridge.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface OpenFoodFactsApi {
    @GET("product/{code}")
    suspend fun fetchProductByCode(@Path("code") code: String): Response<Product>
}
