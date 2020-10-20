package com.devlomi.ayaturabbi.ui.download

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.extensions.navigateSafely
import com.devlomi.ayaturabbi.network.DownloadService
import com.devlomi.ayaturabbi.network.DownloadingResource
import com.devlomi.ayaturabbi.util.IntentConstants
import com.devlomi.ayaturabbi.util.ProperSizeCalc
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.download_fragment.*
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DownloadFragment : Fragment(R.layout.download_fragment) {

    @Inject
    lateinit var properSizeCalc: ProperSizeCalc

    private val viewModel: DownloadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        subscribeObservers()

        viewModel.startDownloading()

        //TODO FIGURE OUT WHY START DOWNLOADING IS CALLED ONSCREEN ROTATION


        btn_cancel_retry.setOnClickListener {
            viewModel.stopDownloading()
        }
    }

    private fun stopDownloading() {
        requireContext().startService(
            Intent(
                requireContext(),
                DownloadService::class.java
            ).apply {
                action = IntentConstants.ACTION_CANCEL_DOWNLOAD
            })
    }

    private fun startDownloading() {
        val filePath = File(requireContext().cacheDir, "test.zip")

        requireContext().startService(Intent(requireContext(), DownloadService::class.java).apply {
            action = IntentConstants.ACTION_START_DOWNLOAD
            putExtra(
                IntentConstants.EXTRA_WIDTH,
                properSizeCalc.getProperWidth(requireActivity().deviceWidthPixels())
            )
            putExtra(IntentConstants.EXTRA_DOWNLOAD_FILE_PATH, filePath.path)
        })
    }

    private fun subscribeObservers() {
        DownloadService.downloadingLiveData.observe(viewLifecycleOwner, {
            when (it) {
                is DownloadingResource.Loading -> progress_download.progress = it.progress
                is DownloadingResource.Error -> {
                    Log.d(

                        "3llomi",
                        "error liveData ${it.e.localizedMessage}"
                    )

                    viewModel.downloadFinished()

                }
                is DownloadingResource.Success -> {
                    viewModel.downloadFinished()

                    Log.d("3llomi", "SUCCESS :) ")
                    findNavController().navigateSafely(R.id.quranPage,R.id.action_downloadFragment_to_quranPage)
                }
            }

        })


        viewModel.startDownloadLiveData.observe(viewLifecycleOwner, {
            startDownloading()
        })

        viewModel.stopDownloadingLiveData.observe(viewLifecycleOwner, {
            stopDownloading()
        })


    }
}