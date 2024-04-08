package com.develop.nowasteinmyfridge.data.repository

import com.develop.nowasteinmyfridge.data.model.GroceryList
import com.develop.nowasteinmyfridge.data.model.GroceryListCreate
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.data.model.IngredientCreate
import com.develop.nowasteinmyfridge.data.model.UserCreate
import com.develop.nowasteinmyfridge.data.model.UserProfile
import com.develop.nowasteinmyfridge.util.Result
import kotlinx.coroutines.flow.Flow
interface FirebaseFirestoreRepository {
    suspend fun getIngredient(): List<Ingredient>
    suspend fun addIngredient(ingredient: IngredientCreate)
    suspend fun getUserInfo(): Flow<Result<UserProfile>>
    suspend fun createUser(userCreate: UserCreate): Flow<Result<Unit>>

    suspend fun getGroceryList(): List<GroceryList>
    suspend fun addGroceryList(groceryList: GroceryListCreate)

    suspend fun clearGroceryList()

    suspend fun deleteIngredient(ingredientID: String): Result<Unit>

    suspend fun updateIngredientQuantity(ingredientID: String, newQuantity: Int): Result<Unit>
}
