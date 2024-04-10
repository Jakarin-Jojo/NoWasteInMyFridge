package com.develop.nowasteinmyfridge.domain

import android.util.Log
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import javax.inject.Inject

class GetIngredientUsedUseCase @Inject constructor(
    private val firestoreRepository: FirebaseFirestoreRepository,
) {
    suspend operator fun invoke(): List<Ingredient> {
        return firestoreRepository.getIngredientUsed()
    }
}