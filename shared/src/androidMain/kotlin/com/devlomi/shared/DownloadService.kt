package com.devlomi.shared

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ayaturabbi.shared.generated.resources.Res
import co.touchlab.kermit.Logger
import com.devlomi.shared.constants.IntentConstants
import com.devlomi.shared.data.db.DBFileNames
import com.devlomi.shared.data.network.DownloadRepository
import com.devlomi.shared.data.network.DownloadingResource
import com.devlomi.shared.data.network.exceptions.UserCancelledException
import com.devlomi.shared.domain.ExtractAndCopyFiles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import okio.FileSystem
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import kotlin.getValue


class DownloadService : ScopedService() {
    val downloadRepository: DownloadRepository by inject()


    private var notification: NotificationCompat.Builder? = null

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private var downloadCancelled = false
    private var downloadJob: Job? = null

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        lifecycleScope.launch {
            downloadRepository.downloadResource.collectLatest {
                if (it is DownloadingResource.Loading) {
                    if (!downloadCancelled) {
                        updateNotificationProgress(it.progress)
                    }
                }
            }
        }
    }

    private fun updateNotificationProgress(progress: Int) {
        notification?.let { notification ->
            notification.setProgress(MAX_PROGRESS, progress, false)
            notification.setContentText(getString(R.string.downloaded, progress))

            if (isApi33OrAbove() && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
    }

    companion object {
        fun start(width: Int, filePath: String, context: Context) {
            val intent = Intent(context, DownloadService::class.java)
            intent.action = IntentConstants.ACTION_START_DOWNLOAD
            intent.putExtra(IntentConstants.EXTRA_WIDTH, width)
            intent.putExtra(IntentConstants.EXTRA_DOWNLOAD_FILE_PATH, filePath)
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            Intent(context, DownloadService::class.java).apply {
                action = IntentConstants.ACTION_CANCEL_DOWNLOAD
                context.startService(
                    this
                )
            }
        }

        private const val NOTIFICATION_CHANNEL_ID = "2"
        private const val NOTIFICATION_ID = 2
        private const val MAX_PROGRESS = 100

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.action?.let { action ->
            if (action == IntentConstants.ACTION_START_DOWNLOAD) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel()
                }




                notification =
                    NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(getString(R.string.downloading_quran_files))
                        .setContentText(getString(R.string.downloaded, 0))
                        .setSmallIcon(R.drawable.ic_noti)
                        .setProgress(MAX_PROGRESS, 0, false)
                        .setSilent(true)

                ServiceCompat.startForeground(
                    this,
                    NOTIFICATION_ID,
                    notification!!.build(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )

                val width = intent?.getIntExtra(IntentConstants.EXTRA_WIDTH, 1260)!!
                val filePath = intent?.getStringExtra(IntentConstants.EXTRA_DOWNLOAD_FILE_PATH)!!
                startDownloading(width, filePath)

            } else if (action == IntentConstants.ACTION_CANCEL_DOWNLOAD) {
                cancelDownload()
            }
        }

        return START_NOT_STICKY

    }

    private fun cancelDownload() {
        downloadCancelled = true
        downloadJob?.cancel()
        lifecycleScope.launch {
            downloadRepository.cancelDownload()
        }
        cancel("Cancelled by user")
        stopService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val downloadNotificationString = getString(R.string.download_notification)
        val channel =
            NotificationChannel(NOTIFICATION_CHANNEL_ID, downloadNotificationString, importance)
        channel.description = downloadNotificationString

        // Don't see these lines in your code...
        val notificationManager = getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun stopService() {
        downloadCancelled = true
        stopForeground(STOP_FOREGROUND_REMOVE)
        notificationManager.cancel(NOTIFICATION_ID)
        stopSelf()
    }

    private fun startDownloading(width: Int, filePath: String) {
        downloadCancelled = false
        downloadJob?.cancel()
        downloadJob = launch(IO) {
            try {
                val result = downloadRepository.download(width, filePath)
                if (result.isSuccess) {
                    stopService()
                } else {
                    throw result.exceptionOrNull() ?: Exception("Download Error")
                }

            } catch (e: Exception) {
                stopService()

            }
        }
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        cancelDownload()
        super.onTaskRemoved(rootIntent)
    }
}
