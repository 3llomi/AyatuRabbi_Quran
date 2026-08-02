package com.devlomi.shared.data.network

import com.devlomi.shared.data.network.FirebaseFileDownloader
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
Due to compilation issues on iosX64 target in Xcode, we couldn't use the shared getFile extension function for downloading files from Firebase Storage.
So, we had to implement the download logic separately for Android and iOS platforms.
This could be fixed when moving to an ARM-based Mac, as the iosX64 target will be deprecated in favor of iosSimulatorArm64.
 */
class DownloadRepository(private val firebaseFileDownloader: FirebaseFileDownloader) {

    private val _downloadResource = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
    val downloadResource: StateFlow<DownloadingResource> get() = _downloadResource.asStateFlow()


    private var file: PlatformFile? = null
//    private var downloadTask: FileDownloadTask? = null

    suspend fun cancelDownload() {

//        downloadTask?.cancel()//TODO
        file?.delete()
    }

    suspend fun download(width: Int, path: String) {
//        downloadTask?.cancel()//TODO

//        val ref = FirebaseStorage.getInstance().getReference("quran_files/data_${width}.zip")
        file = PlatformFile(path)
        try {
            val result =
                firebaseFileDownloader.downlaodFile("quran_files/data_${width}.zip", file!!.path) {

                    //TODO
//                val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount
//
//                //get progress
//                val progress = progressDouble.toInt()
                    _downloadResource.value = DownloadingResource.Loading(it)
                }
            if (result.isSuccess) {
                _downloadResource.value = DownloadingResource.Success
            } else {
                //TODO TEST CAST
                _downloadResource.value =
                    DownloadingResource.Error((result.exceptionOrNull() ?: Exception("Download Error")) as Exception)
            }
        } catch (e: Exception) {
            _downloadResource.value = DownloadingResource.Error(e)
        }


//        downloadTask = ref.getFile(file!!)
//        downloadTask?.addOnProgressListener {
//
//            val progressDouble = 100.0 * it.bytesTransferred / it.totalByteCount
//
//            get progress
//            val progress = progressDouble.toInt()
//            _downloadResource.value = DownloadingResource.Loading(progress)
//
//        }?.addOnCanceledListener {
//            file?.delete()
//        }?.addOnFailureListener {
//            file?.delete()
//        }?.await()
    }


}