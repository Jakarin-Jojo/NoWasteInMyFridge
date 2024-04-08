package com.develop.nowasteinmyfridge.domain

import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class UpdateIngredientQuantityUseCase @Inject constructor(
    private val firestoreRepository: FirebaseFirestoreRepository
) {
    suspend operator fun invoke(ingredientID: String, newQuantity: Int) {
        firestoreRepository.updateIngredientQuantity(ingredientID, newQuantity)
    }
}
