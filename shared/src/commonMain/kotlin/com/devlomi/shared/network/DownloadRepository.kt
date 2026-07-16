package com.devlomi.shared.network

import com.devlomi.shared.getFile
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DownloadRepository() {


    private val _downloadResource = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
    @NativeCoroutinesState
    val downloadResource: StateFlow<DownloadingResource> get() = _downloadResource.asStateFlow()



    private var filePath: String? = null
//    private var downloadTask: DownloadTask? = null

    fun cancelDownload() {
//        file?.delete()
        //TODO
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



}