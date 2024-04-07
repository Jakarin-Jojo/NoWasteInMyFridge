package com.develop.nowasteinmyfridge.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.develop.nowasteinmyfridge.NotificationUtils
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.domain.CheckExpirationUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExpiryCheckWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkExpirationUseCase: CheckExpirationUseCase,
    private val notificationUtils: NotificationUtils,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val expiringIngredients = checkExpirationUseCase()
            if (expiringIngredients.isNotEmpty()) {
                // Trigger notification
                val message = buildNotificationMessage(expiringIngredients)
                notificationUtils.showNotification(message)
            }
            Log.d("ExpiryCheckWorker", "ExpiryCheckWorker: Task completed successfully")
            Result.success()
        } catch (e: Exception) {
            // Handle exceptions here
            Log.e(
                "ExpiryCheckWorker",
                "ExpiryCheckWorker: Task failed with exception: ${e.message}"
            )
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun buildNotificationMessage(expiringIngredients: List<Ingredient>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("These ingredients are expiring soon:\n")
        for (ingredient in expiringIngredients) {
            stringBuilder.append("- ${ingredient.name}: The expiration date is ${ingredient.efd}\n")
        }
        return stringBuilder.toString()
    }
}
