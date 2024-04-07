package com.develop.nowasteinmyfridge

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.develop.nowasteinmyfridge.workers.ExpirationCheckScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class NoWasteInMyFridgeApp : Application(), Configuration.Provider {
    @Inject
    lateinit var expirationCheckScheduler: ExpirationCheckScheduler
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        scheduleDailyCheck()
    }

    private fun scheduleDailyCheck() {
        expirationCheckScheduler.scheduleDailyCheck()
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}