package com.develop.nowasteinmyfridge.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class FoodWasteReportScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun scheduleWeeklyReport() {
        val workRequest = buildWeeklyCheckWorkRequest()
        Log.e("FoodWasteReportScheduler", "FoodWasteReportScheduler is working")
        enqueuePeriodicWorkRequest(workRequest)
    }

    private fun buildWeeklyCheckWorkRequest(): PeriodicWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Schedule the work to repeat every day at 11:30 PM
        return PeriodicWorkRequestBuilder<FoodWasteReportWorker>(
            repeatInterval = 7, // repeat every 7 days
            repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .addTag(FoodWasteReportWorker.TAG)
            .build()
    }

    private fun calculateInitialDelay(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val targetTimeMillis = getTargetTimeMillis()
        return if (targetTimeMillis <= currentTimeMillis) {
            targetTimeMillis + 7 * 24 * 60 * 60 * 1000 - currentTimeMillis
        } else {
            targetTimeMillis - currentTimeMillis
        }
    }

    private fun getTargetTimeMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    private fun enqueuePeriodicWorkRequest(workRequest: PeriodicWorkRequest) {
        try {
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "FoodWasteReportWorker",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                workRequest,
            )
            Log.d("FoodWasteReportScheduler", "FoodWaste Report successfully")
        } catch (e: Exception) {
            Log.e("FoodWasteReportScheduler", "Error Report FoodWaste weekly : ${e.message}")
            e.printStackTrace()
        }
    }
}