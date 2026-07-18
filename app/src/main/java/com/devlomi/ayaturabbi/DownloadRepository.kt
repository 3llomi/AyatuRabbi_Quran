package com.devlomi.ayaturabbi

import com.devlomi.shared.getFile
import com.devlomi.shared.network.DownloadingResource
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.io.File

/*
Due to compilation issues on iosX64 target in Xcode, we couldn't use the shared getFile extension function for downloading files from Firebase Storage.
So, we had to implement the download logic separately for Android and iOS platforms.
This could be fixed when moving to an ARM-based Mac, as the iosX64 target will be deprecated in favor of iosSimulatorArm64.
 */
class DownloadRepository {

    private val _downloadResource = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
    val downloadResource: StateFlow<DownloadingResource> get() = _downloadResource.asStateFlow()



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
            _downloadResource.value = DownloadingResource.Loading(progress)

        }?.addOnCanceledListener {
            file?.delete()
        }?.addOnFailureListener {
            file?.delete()
        }?.await()
    }


}