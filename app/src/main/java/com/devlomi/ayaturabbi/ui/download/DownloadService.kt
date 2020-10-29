package com.devlomi.ayaturabbi.ui.download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.IntentConstants
import com.devlomi.ayaturabbi.ScopedService
import com.devlomi.ayaturabbi.db.DBFileNames
import com.devlomi.ayaturabbi.network.DownloadRepository
import com.devlomi.ayaturabbi.network.DownloadingResource
import com.devlomi.ayaturabbi.network.exceptions.UserCancelledException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class DownloadService : ScopedService() {

    @Inject
    lateinit var downloadRepository: DownloadRepository


    private var notification: NotificationCompat.Builder? = null

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private var downloadCancelled = false

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
        downloadRepository.downloadLiveData.observe(this) {
            if (it is DownloadingResource.Loading) {
                if (!downloadCancelled) {
                    updateNotificationProgress(it.progress)
                    _downloadingLiveData.value = it
                }
            }
        }
    }

    private fun updateNotificationProgress(progress: Int) {
        notification?.let { notification ->
            notification.setProgress(MAX_PROGRESS, progress, false)
            notification.setContentText(getString(R.string.downloaded, progress))
            notificationManager.notify(NOTIFICATION_ID, notification.build())
        }
    }

    companion object {
        private val _downloadingLiveData = MutableLiveData<DownloadingResource>()
        val downloadingLiveData: LiveData<DownloadingResource>
            get() = _downloadingLiveData

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
                        .setNotificationSilent()

                startForeground(NOTIFICATION_ID, notification!!.build())

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
        downloadRepository.cancelDownload()
        cancel("Cancelled by user")
        _downloadingLiveData.value = DownloadingResource.Error(UserCancelledException())
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
        stopForeground(true)
        notificationManager.cancel(NOTIFICATION_ID)
        stopSelf()
    }

    private fun startDownloading(width: Int, filePath: String) {
        downloadCancelled = false
        launch(IO) {
            try {
                downloadRepository.download(width, filePath)
                val temp = File("$cacheDir/quran_data/")

                downloadRepository.unZipFile(filePath, temp.path)


                copyFiles(temp, width)


                temp.deleteRecursively()
                File(cacheDir, "data.zip").delete()





                withContext(Dispatchers.Main) {
                    _downloadingLiveData.value = DownloadingResource.Success
                }
                stopService()

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _downloadingLiveData.value = DownloadingResource.Error(e)
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
