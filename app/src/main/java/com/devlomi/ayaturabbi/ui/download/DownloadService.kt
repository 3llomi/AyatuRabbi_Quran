package com.devlomi.ayaturabbi.ui.download

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
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
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.IntentConstants
import com.devlomi.ayaturabbi.ScopedService
import com.devlomi.shared.network.DownloadRepository
import com.devlomi.shared.network.DownloadingResource
import com.devlomi.shared.network.exceptions.UserCancelledException
import com.devlomi.ayaturabbi.util.isApi33OrAbove
import com.devlomi.shared.db.DBFileNames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.io.File


class DownloadService : ScopedService() {
//TODO RESOLVE CRASH:
    // Reason: A foreground service of FOREGROUND_SERVICE_TYPE_SHORT_SERVICE did not stop within a timeout: ComponentInfo{com.devlomi.ayaturabbi/com.devlomi.ayaturabbi.ui.download.DownloadService}
    val downloadRepository: DownloadRepository by inject()


    private var notification: NotificationCompat.Builder? = null

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private var downloadCancelled = false
    private var downloadJob: Job?=null

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        lifecycleScope.launch {
            downloadRepository.downloadResource.collectLatest {
                if (it is DownloadingResource.Loading) {
                    if (!downloadCancelled) {
                        updateNotificationProgress(it.progress)
                        _downloadState.value = it
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
        private val _downloadState = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
        val downloadState: StateFlow<DownloadingResource>
            get() = _downloadState

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
//                        .setSmallIcon(R.drawable.ic_noti)//TODO
                        .setSmallIcon(R.drawable.ic_note)//TODO
//                        .setProgress(MAX_PROGRESS, 0, false)//TODO
                        .setSilent(true)

                ServiceCompat.startForeground(
                    this,
                    NOTIFICATION_ID,
                    notification!!.build(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
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
        downloadRepository.cancelDownload()
        cancel("Cancelled by user")
        _downloadState.value = DownloadingResource.Error(UserCancelledException())
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
        //TODO RESOLVE A CRASH WHERE THE DOWNLOAD IS CANCELLED AND THE USER STARTS ANOTHER DOWNLOAD, IT CRASHES THE APP
        // java.lang.IllegalStateException: Already resumed, but proposed with update Success(/data/user/0/com.devlomi.ayaturabbi/cache/data.zip)
        // 	at kotlinx.coroutines.CancellableContinuationImpl.alreadyResumedError(CancellableContinuationImpl.kt:556)
        // 	at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl$kotlinx_coroutines_core(CancellableContinuationImpl.kt:521)
        // 	at kotlinx.coroutines.CancellableContinuationImpl.resumeImpl$kotlinx_coroutines_core$default(CancellableContinuationImpl.kt:493)
        // 	at kotlinx.coroutines.CancellableContinuationImpl.resumeWith(CancellableContinuationImpl.kt:359)
        downloadJob = launch(IO) {
            try {
                Log.d("DownloadService","Downloading... at filePath ${filePath}")
                downloadRepository.download(width, filePath)
                val temp = File("$cacheDir/quran_data/")

                Log.d("DownloadService","Unzipping")
                downloadRepository.unZipFile(filePath, temp.path)

                Log.d("DownloadService","Copying files...")
                copyFiles(temp, width)

                Log.d("DownloadService","Deleting temp...")
                temp.deleteRecursively()
                Log.d("DownloadService","Copied files")
                File(cacheDir, "data.zip").delete()





                withContext(Dispatchers.Main) {
                    _downloadState.value = DownloadingResource.Success
                }
                stopService()

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _downloadState.value = DownloadingResource.Error(e)
                }

                stopService()

            }
        }
    }


    private fun copyFiles(temp: File, width: Int) {
        File(temp, DBFileNames.ayahInfoNameDbPath(width)).copyTo(
            File(
                filesDir,
                DBFileNames.ayahInfoNameDbPath(width)
            ), overwrite = true
        )


        File(temp, DBFileNames.quranDbPath).copyTo(
            File(
                filesDir,
                DBFileNames.quranDbPath
            ), overwrite = true
        )



        File(temp, "width_$width").copyRecursively(
            File(filesDir, "quran_images"),
            overwrite = true
        )
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        cancelDownload()
        super.onTaskRemoved(rootIntent)
    }
}
