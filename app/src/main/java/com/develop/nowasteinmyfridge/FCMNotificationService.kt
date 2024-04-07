package com.develop.nowasteinmyfridge

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class FCMNotificationService : FirebaseMessagingService(){
    companion object {
        private const val TAG = "FCMNotificationService"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if the message contains a notification payload.
        remoteMessage.notification?.let {
            val messageBody = it.body
            if (messageBody != null) {
                val notificationUtils = NotificationUtils(applicationContext)
                notificationUtils.showNotification(messageBody)
            }
        }
    }
}