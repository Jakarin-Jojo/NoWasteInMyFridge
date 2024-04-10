package com.develop.nowasteinmyfridge.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.develop.nowasteinmyfridge.MainActivity
import com.develop.nowasteinmyfridge.R
import com.develop.nowasteinmyfridge.data.model.Ingredient
import com.develop.nowasteinmyfridge.domain.AddPerformanceUsingIngredientUseCase
import com.develop.nowasteinmyfridge.domain.GetFoodWasteReportUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class FoodWasteReportWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val addPerformanceUsingIngredientUseCase: AddPerformanceUsingIngredientUseCase,
    private val getFoodWasteReportUseCase: GetFoodWasteReportUseCase,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            addPerformanceUsingIngredientUseCase.invoke()
            val performance = getFoodWasteReportUseCase.invoke()
            sendNotification("Now has the performance of reducing food waste by ${performance.last().performance} %")
            Log.d("FoodWasteReportWorker", "FoodWasteReportWorker: Task completed successfully")
            Result.success()
        } catch (e:Exception){
            // Handle exceptions here
            Log.e(
                "FoodWasteReportWorker",
                "FoodWasteReportWorker: Task failed with exception: ${e.message}"
            )
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun buildNotificationMessage(expiringIngredients: List<Ingredient>): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("These are the Ingredients you have used this week:\n")
        for (ingredient in expiringIngredients) {
            stringBuilder.append("- ${ingredient.name}: ${ingredient.quantity} were used\n")
        }
        return stringBuilder.toString()
    }

    private fun sendNotification(message:String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (for Android Oreo and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "food_waste_report_channel"
            val channelName = "Food Waste Report"
            val channelDescription = "Notifications for food waste report channel"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
                enableLights(true)
                lightColor = 0xffffff
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
                setShowBadge(true) // Show notification badge on app icon
            }

            notificationManager.createNotificationChannel(channel)
        }

        // Create the notification intent
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        // Create a vibration pattern
        val vibrationPattern = longArrayOf(0, 400, 200, 400)

        // Create the notification
        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, "food_waste_report_channel")
                .setContentTitle("Weekly food waste report")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Set small icon here
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setVibrate(vibrationPattern) // Vibrate
                .setLights(0xff00ff00.toInt(), 300, 1000) // LED notification light
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // Assign category for notification

        // Show the notification
        val notificationId = 124
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        const val TAG = "FoodWasteNotificationWorker"
    }

}