package com.devlomi.shared.data.network

import co.touchlab.kermit.Logger
import com.devlomi.shared.data.network.FirebaseFileDownloader
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.domain.ExtractAndCopyFiles
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.path
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/*
//TODO REMOVE THIS NOTICE
Due to compilation issues on iosX64 target in Xcode, we couldn't use the shared getFile extension function for downloading files from Firebase Storage.
So, we had to implement the download logic separately for Android and iOS platforms.
This could be fixed when moving to an ARM-based Mac, as the iosX64 target will be deprecated in favor of iosSimulatorArm64.
 */
class DownloadRepository(
    private val firebaseFileDownloader: FirebaseFileDownloader,
    private val extractAndCopyFiles: ExtractAndCopyFiles,
    private val settingsRepository: SettingsRepository,
) {

    private val _downloadResource = MutableStateFlow<DownloadingResource>(DownloadingResource.None)
    val downloadResource: StateFlow<DownloadingResource> get() = _downloadResource.asStateFlow()

    private var file: PlatformFile? = null

    suspend fun cancelDownload() {
        firebaseFileDownloader.cancel()
        file?.delete()
    }

    suspend fun download(width: Int, path: String): Result<String> {
        file = PlatformFile(path)
        Logger.d { "PlatformFile Path ${file?.path}" }
        //TODO DOWNLOAD NOT COMPLETING
        extractAndCopyFiles.execute(width, file!!.path)
        Logger.d { "Files Copied, attemtping to set downlaod finished" }
        settingsRepository.setDownloadFinished(true)
        return Result.success("Download and extraction successful")
        try {
            val result =
                firebaseFileDownloader.downlaodFile(
                    "quran_files/data_${width}.zip",
                    file!!.path
                ) {
                    if (downloadResource.value !is DownloadingResource.Success && downloadResource.value !is DownloadingResource.Error) {
                        _downloadResource.value = DownloadingResource.Loading(it)
                    }

                }
            if (result.isSuccess) {
                Logger.d { "Result Success" }
                extractAndCopyFiles.execute(width, file!!.path)
                settingsRepository.setDownloadFinished(true)
                _downloadResource.value = DownloadingResource.Success
                return Result.success("Download and extraction successful")
            } else {
                //TODO TEST CAST
                _downloadResource.value =
                    DownloadingResource.Error(
                        (result.exceptionOrNull()
                            ?: Exception("Download Error")) as Exception
                    )
                return Result.failure(Exception("Download Error"))

            }
        } catch (e: Exception) {
            Logger.d { "Download Failure Repository ${e.message}" }
            _downloadResource.value = DownloadingResource.Error(e)
            Logger.d { "Submitted Error Event - Download Repository ${e.message}" }
            return Result.failure(Exception(e.message))
        }
    }


}