package com.devlomi.ayaturabbi

import android.app.Service
import androidx.lifecycle.LifecycleService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class ScopedService : LifecycleService(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate() {
        super.onCreate()
        job = Job()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}