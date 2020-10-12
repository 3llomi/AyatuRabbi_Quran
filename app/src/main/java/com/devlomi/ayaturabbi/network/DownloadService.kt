package com.devlomi.ayaturabbi.network

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devlomi.ayaturabbi.util.IntentConstants
import com.devlomi.ayaturabbi.ScopedService
import com.devlomi.ayaturabbi.db.DBFileNames
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoDao
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

    @Inject
    lateinit var ayahInfoDao: AyahInfoDao


    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    override fun onCreate() {
        super.onCreate()
        downloadRepository.downloadLiveData.observe(this, {
            if (it is DownloadingResource.Loading) {
                _downloadingLiveData.value = it
            }
        })
    }

    companion object {
        private val _downloadingLiveData = MutableLiveData<DownloadingResource>()
        val downloadingLiveData: LiveData<DownloadingResource>
            get() = _downloadingLiveData


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.action?.let { action ->
            if (action == IntentConstants.ACTION_START_DOWNLOAD) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel("2", "name", importance)
                    channel.description = "description"

                    // Don't see these lines in your code...
                    val notificationManager = getSystemService(
                        NotificationManager::class.java
                    )
                    notificationManager.createNotificationChannel(channel)
                }
                val notification =
                    NotificationCompat.Builder(this, "2")
                        .setContentText("Content")
                        .setContentTitle("TITLE")
                        .setSmallIcon(R.drawable.sym_def_app_icon)
                        .build()
                //TODO UPDATE NOTIFIATION PROGRESS
                startForeground(1, notification)
                val width = intent?.getIntExtra(IntentConstants.EXTRA_WIDTH, 1260)!!
                val filePath = intent?.getStringExtra(IntentConstants.EXTRA_DOWNLOAD_FILE_PATH)!!
                startDownloading(width, filePath)
            } else if (action == IntentConstants.ACTION_CANCEL_DOWNLOAD) {
                cancel("Cancelled by user")
            }
        }

        return START_NOT_STICKY

    }

    private fun startDownloading(width: Int, filePath: String) {
        launch(IO) {
            try {
                downloadRepository.download(width, filePath)
                val temp = File("$cacheDir/quran_data/")

                downloadRepository.unZipFile(filePath, temp.path)


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


                val copySuccess =
                    File(temp, "width_$width").copyRecursively(
                        File(filesDir, "quran_images"),
                        overwrite = true
                    )







                Log.d("3llomi", "finished download $filePath")
                withContext(Dispatchers.Main) {
                    _downloadingLiveData.value = DownloadingResource.Success
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _downloadingLiveData.value = DownloadingResource.Error(e)
                }
                Log.d("3llomi", "error downloading ${e.localizedMessage}")
            }
        }
    }
}
