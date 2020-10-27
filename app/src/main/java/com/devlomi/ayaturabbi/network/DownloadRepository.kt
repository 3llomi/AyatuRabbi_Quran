package com.devlomi.ayaturabbi.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devlomi.ayaturabbi.extensions.unzip
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

class DownloadRepository @Inject constructor() {


    private val _downloadLiveData = MutableLiveData<DownloadingResource>()

    val downloadLiveData: LiveData<DownloadingResource>
        get() = _downloadLiveData


    private var file: File? = null
    private var downloadTask: FileDownloadTask? = null

    fun cancelDownload() {
        downloadTask?.cancel()
        file?.delete()
    }

    suspend fun download(width: Int, path: String) {
        downloadTask?.cancel()

        val ref = FirebaseStorage.getInstance().getReference("quran_files/data_${width}.zip")
        file = File(path)

        downloadTask = ref.getFile(file!!)
        downloadTask?.addOnProgressListener {

            val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount

            //get progress
            val progress = progressDouble.toInt()
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