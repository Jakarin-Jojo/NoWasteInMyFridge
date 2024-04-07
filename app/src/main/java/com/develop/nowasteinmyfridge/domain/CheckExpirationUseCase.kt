package com.develop.nowasteinmyfridge.domain

import com.develop.nowasteinmyfridge.data.model.Ingredient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CheckExpirationUseCase @Inject constructor(
    private val getIngredientsUseCase: GetIngredientsUseCase,
) {
    suspend operator fun invoke(expirationPeriodInDays: Int = 3): List<Ingredient> {
        val ingredients = getIngredientsUseCase.invoke()
        val expiringIngredients = ingredients.filter { ingredient ->
            // Parse the expiration date from String to Date
            val expirationDate = parseDate(ingredient.efd)
            expirationDate != null && isWithinExpirationPeriod(
                expirationDate,
                expirationPeriodInDays
            )
        }
        return expiringIngredients
    }

    private fun parseDate(dateString: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun isWithinExpirationPeriod(expirationDate: Date, periodInDays: Int): Boolean {
        val currentDate = Calendar.getInstance().time
        val calendar = Calendar.getInstance().apply { time = expirationDate }
        calendar.add(Calendar.DATE, -periodInDays)
        val expirationThreshold = calendar.time
        return currentDate.before(expirationDate) && currentDate.after(expirationThreshold)
    }
}