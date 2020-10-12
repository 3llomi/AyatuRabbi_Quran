package com.devlomi.ayaturabbi.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devlomi.ayaturabbi.extensions.unzip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DownloadRepository constructor(private val webService: APIWebService) {
    companion object {
        const val BASE_URL = "http://192.168.64.2/quran/"
    }

    private val _downloadLiveData = MutableLiveData<DownloadingResource>()

    val downloadLiveData: LiveData<DownloadingResource>
        get() = _downloadLiveData


    suspend fun download(width: Int, path: String) {
        val url = "${BASE_URL}images_${width}.zip"
        Log.d("3llomi", "url is $url")
        val responseBody = webService.downloadFile(url).body()
        saveFile(responseBody, path)
    }

    fun unZipFile(zipFilePath:String,targetLocation:String){
        val file = File(targetLocation)
        if (file.exists()){
            file.mkdirs()
        }
        Log.d("3llomi","zipFilePath $zipFilePath targetLocation $targetLocation")
        File(zipFilePath).unzip(File(targetLocation))
    }


    private suspend fun saveFile(body: ResponseBody?, path: String) {
        if (body == null) {

            emitEventOnMainThread(
                DownloadingResource.Error(
                    IllegalArgumentException()
                )
            )//TODO CREATE NEW EXCEPTION CLASS HERE

            return
        }
        var input: InputStream? = null
        try {
            input = body.byteStream()
            val fullFileSize = body.contentLength()
            //reset
            emitEventOnMainThread(DownloadingResource.Loading(0))

            val fos = FileOutputStream(path)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                var downloadedBytes = 0L
                var lastProgress = -1
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    downloadedBytes += read

                    val progress = (downloadedBytes * 100 / fullFileSize).toInt()
                    //prevent duplicate progresses
                    if (lastProgress != progress){
                        delay(1000)
                        emitEventOnMainThread(DownloadingResource.Loading(progress))
                        lastProgress = progress
                    }
                }
                output.flush()
                emitEventOnMainThread(DownloadingResource.Success)

            }
        } catch (e: Exception) {
            emitEventOnMainThread(DownloadingResource.Error(e))
            Log.d("3llomi", "error $e")
        } finally {
            input?.close()
        }
    }

    private suspend fun emitEventOnMainThread(event: DownloadingResource) {
        withContext(Dispatchers.Main) {
            _downloadLiveData.value = event
        }
    }
}