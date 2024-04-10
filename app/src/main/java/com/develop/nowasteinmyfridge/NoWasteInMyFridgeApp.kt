package com.develop.nowasteinmyfridge

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.develop.nowasteinmyfridge.workers.ExpirationCheckScheduler
import com.develop.nowasteinmyfridge.workers.FoodWasteReportScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class NoWasteInMyFridgeApp : Application(), Configuration.Provider {
    @Inject
    lateinit var expirationCheckScheduler: ExpirationCheckScheduler
    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    @Inject
    lateinit var foodWasteReportScheduler: FoodWasteReportScheduler

    override fun onCreate() {
        super.onCreate()
        scheduleDailyCheck()
        scheduleFoodWasteReport()
    }

    private fun scheduleDailyCheck() {
        expirationCheckScheduler.scheduleDailyCheck()
    }

    private fun scheduleFoodWasteReport() {
        foodWasteReportScheduler.scheduleWeeklyReport()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}