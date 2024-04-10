package com.develop.nowasteinmyfridge

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMNotificationService : FirebaseMessagingService() {
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
                val notificationExpiringIngredientsUtils = NotificationExpiringIngredientsUtils(applicationContext)
                notificationExpiringIngredientsUtils.showNotification(messageBody)
            }
        }
    }
}