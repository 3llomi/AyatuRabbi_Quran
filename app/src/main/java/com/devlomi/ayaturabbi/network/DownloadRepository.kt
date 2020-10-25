package com.devlomi.ayaturabbi.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devlomi.ayaturabbi.extensions.unzip
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class DownloadRepository @Inject constructor() {


    private val _downloadLiveData = MutableLiveData<DownloadingResource>()

    val downloadLiveData: LiveData<DownloadingResource>
        get() = _downloadLiveData


    private var file: File? = null
    private var task: FileDownloadTask? = null

    fun cancelDownload() {
        task?.cancel()
        file?.delete()
    }

    suspend fun downloadFB(width: Int, path: String) {
        task?.cancel()

        val ref = FirebaseStorage.getInstance().getReference("quran_files/data_${width}.zip")
        file = File(path)

        task = ref.getFile(file!!)
        task?.addOnProgressListener {

            val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount

            //get progress
            val progress = progressDouble.toInt()
Log.d("3llomi","progress $progress")
            _downloadLiveData.value = DownloadingResource.Loading(progress)

        }?.addOnCanceledListener {
            file?.delete()
        }?.addOnFailureListener {
            file?.delete()
        }?.await()
    }

    fun unZipFile(zipFilePath: String, targetLocation: String) {
        val file = File(targetLocation)
        if (file.exists()) {
            file.mkdirs()
        }
        File(zipFilePath).unzip(File(targetLocation))
    }


}