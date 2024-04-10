package com.develop.nowasteinmyfridge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class NotificationExpiringIngredientsUtils @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun showNotification(message: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel (for Android Oreo and higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "expiring_ingredients_channel"
            val channelName = "Expiring Ingredients"
            val channelDescription = "Notifications for expiring ingredients"
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
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )


        // Create a vibration pattern
        val vibrationPattern = longArrayOf(0, 400, 200, 400)

        // Create the notification
        val notificationBuilder =
            NotificationCompat.Builder(context, "expiring_ingredients_channel")
                .setContentTitle("Expiring Ingredients")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Set small icon here
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(vibrationPattern) // Vibrate
                .setLights(0xff00ff00.toInt(), 300, 1000) // LED notification light
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // Assign category for notification

        // Show the notification
        val notificationId = 123
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}