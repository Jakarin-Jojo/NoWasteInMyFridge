package com.develop.nowasteinmyfridge.domain

import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.data.model.Report
import com.develop.nowasteinmyfridge.data.repository.FirebaseFirestoreRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddPerformanceUsingIngredientUseCase @Inject constructor(
private val getIngredientUsedUseCase: GetIngredientUsedUseCase,
private val getIngredientsUseCase: GetIngredientsUseCase,
private val firestoreRepository: FirebaseFirestoreRepository
) {
    suspend operator fun invoke() {
        val ingredientsUsed = getIngredientUsedUseCase()
        val ingredients = getIngredientsUseCase()

        // Filter ingredients that are expired this week
        val expiredThisWeek = ingredients.filter { it.isExpiredThisWeek() }

        // Filter ingredients used that expire this week
        val usedExpiringThisWeek = ingredientsUsed.filter { it.isExpiredThisWeek() }

        // Calculate the percentage of used ingredients that expire this week out of all expired ingredients this week
        val percentage = if (expiredThisWeek.isNotEmpty()) {
            (usedExpiringThisWeek.size.toDouble() / (expiredThisWeek.size.toDouble() + usedExpiringThisWeek.size.toDouble())) * 100
        } else {
            0.0 // Handle division by zero case
        }
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        val formattedTime = dateFormat.format(currentTime)

        val report = Report(
            week = formattedTime,
            performance = percentage,
        )
        firestoreRepository.addPerformance(report)
    }

    private fun Ingredient.isExpiredThisWeek(): Boolean {
        val dateFormat = SimpleDateFormat("yy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false // Ensure strict date parsing

        return try {
            val expiryDate = dateFormat.parse(efd)
            val currentDate = Calendar.getInstance().apply { time = Date() }
            val endOfWeek = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                add(Calendar.WEEK_OF_YEAR, 1)
            }

            expiryDate != null && expiryDate.after(currentDate.time) && expiryDate.before(endOfWeek.time)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}