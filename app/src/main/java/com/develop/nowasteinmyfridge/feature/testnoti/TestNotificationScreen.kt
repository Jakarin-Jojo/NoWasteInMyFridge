package com.develop.nowasteinmyfridge.feature.testnoti

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.develop.nowasteinmyfridge.workers.ExpiryCheckWorker

@Composable
fun TestNotificationScreen() {
    val context = LocalContext.current
    Button(onClick = { enqueueWork(context) }) {
        // Button text
        Text(text = "Test Notify Ingredient close to expire")
    }
}
private fun enqueueWork(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<ExpiryCheckWorker>().build()
    WorkManager.getInstance(context).enqueue(workRequest)
}