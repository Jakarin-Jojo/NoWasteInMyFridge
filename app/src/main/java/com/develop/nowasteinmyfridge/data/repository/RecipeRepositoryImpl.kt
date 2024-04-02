package com.develop.nowasteinmyfridge.data.repository

import com.develop.nowasteinmyfridge.data.api.BASE_URL
import com.develop.nowasteinmyfridge.data.api.EdamamApi
import com.develop.nowasteinmyfridge.data.model.RecipeSearchResponse
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject


class RecipeRepositoryImpl @Inject constructor() : RecipeRepository {
    private val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }
    private val appId: String = dotenv["APP_ID_EDAMANAPI"]
    private val appKey: String = dotenv["APP_KEY_EDAMANAPI"]

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val edamamApi = retrofit.create(EdamamApi::class.java)

    override suspend fun searchRecipes(query: String): RecipeSearchResponse {
        return edamamApi.searchRecipes(
            query = query,
            appId = appId,
            appKey = appKey,
        ).body() ?: throw IOException("Error fetching recipes")
    }
}