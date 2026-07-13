package com.devlomi.shared.network

import com.devlomi.shared.FileUnzipper
import com.devlomi.shared.getFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DownloadRepository(private val fileUnzipper: FileUnzipper) {


    private val _downloadResource = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
    val downloadResource: Flow<DownloadingResource> get() = _downloadResource



    private var filePath: String? = null
//    private var downloadTask: DownloadTask? = null

    fun cancelDownload() {
//        file?.delete()
    }

    suspend fun download(width: Int, path: String) {
//        downloadTask?.cancel()
//        val ref = Firebase.storage.reference("quran_files/data_${width}.zip")
//        file = File(path)
        val result = getFile("quran_files/data_${width}.zip", path){progress ->
            _downloadResource.value = DownloadingResource.Loading(progress.toInt())
        }
        if (!result.isSuccess && result.exceptionOrNull() != null) {
            throw result.exceptionOrNull()!!
        }

    }

    fun unZipFile(zipFilePath: String, targetLocation: String) {
        fileUnzipper.unzip(zipFilePath,targetLocation)
    }


}