package com.develop.nowasteinmyfridge.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExpirationCheckScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun scheduleDailyCheck() {
        val workRequest = buildDailyCheckWorkRequest()
        Log.e("ExpirationCheckScheduler", "ExpirationCheckScheduler is working")
        enqueuePeriodicWorkRequest(workRequest)
    }

    private fun buildDailyCheckWorkRequest(): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Schedule the work to repeat every day at 11:30 PM
        return PeriodicWorkRequestBuilder<ExpiryCheckWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()
    }

    private fun calculateInitialDelay(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val targetTimeMillis = getTargetTimeMillis()
        return if (targetTimeMillis <= currentTimeMillis) {
            // If the target time has already passed for today, schedule it for the next day
            targetTimeMillis + TimeUnit.DAYS.toMillis(1) - currentTimeMillis
        } else {
            targetTimeMillis - currentTimeMillis
        }
    }

    private fun getTargetTimeMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    private fun enqueuePeriodicWorkRequest(workRequest: PeriodicWorkRequest) {
        try {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "ExpiryCheckWorker",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
            Log.d("ExpirationCheckScheduler", "Daily check scheduled successfully")
        } catch (e: Exception) {
            Log.e("ExpirationCheckScheduler", "Error scheduling daily check: ${e.message}")
            e.printStackTrace()
        }
    }
}