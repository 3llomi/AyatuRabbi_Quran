package com.devlomi.ayaturabbi.ui.download

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.extensions.deviceWidthPixels
import com.devlomi.ayaturabbi.extensions.navigateSafely
import com.devlomi.ayaturabbi.network.DownloadingResource
import com.devlomi.ayaturabbi.network.exceptions.UserCancelledException
import com.devlomi.ayaturabbi.constants.IntentConstants
import com.devlomi.ayaturabbi.databinding.DownloadFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class DownloadFragment : Fragment(R.layout.download_fragment) {

    private val viewModel: DownloadViewModel by viewModels()

    private var _binding : DownloadFragmentBinding? = null
    private val binding: DownloadFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DownloadFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        subscribeObservers()

        viewModel.setDeviceWidth(requireActivity().deviceWidthPixels())

        showDownloadDialog()



        binding.btnDownload.setOnClickListener {
            showDownloadDialog()
        }

        binding.btnCancel.setOnClickListener {
            viewModel.stopDownloading()
        }
    }

    private fun showDownloadDialog() {
        MaterialDialog(requireContext()).show {
            title(R.string.download_required_files_title)
            message(R.string.download_required_files_message)
            negativeButton(R.string.cancel) {
                this@DownloadFragment.binding.btnDownload.isVisible = true
            }
            positiveButton(R.string.download) {
                viewModel.startDownloading()
            }
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

    private fun startDownloading(width: Int) {
        binding.progressDownload.progress = 0
        binding.tvDownloading.isVisible = true
        binding.progressDownload.isVisible = true
        binding.btnCancel.isVisible = true
        binding.btnDownload.isVisible = false
        binding.tvDownloading.setText(R.string.downloading_files)

        val filePath = File(requireContext().cacheDir, "data.zip")

        requireContext().startService(Intent(requireContext(), DownloadService::class.java).apply {
            action = IntentConstants.ACTION_START_DOWNLOAD
            putExtra(
                IntentConstants.EXTRA_WIDTH,
                width
            )
            putExtra(IntentConstants.EXTRA_DOWNLOAD_FILE_PATH, filePath.path)
        })
    }

    private fun subscribeObservers() {
        DownloadService.downloadingLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is DownloadingResource.Loading -> binding.progressDownload.progress = it.progress
                is DownloadingResource.Error -> {

                    val errorMessage =
                        if (it.e is UserCancelledException) getString(R.string.download_cancelled) else getString(
                            R.string.download_failed
                        )
                    binding.tvDownloading.text = errorMessage
                    binding.progressDownload.isVisible = false
                    binding.tvDownloading.isVisible = true
                    binding.btnDownload.isVisible = true
                    binding.btnCancel.isVisible = false

                }

                is DownloadingResource.Success -> {
                    viewModel.downloadFinished()

                    navigateToNextDest()
                }

                else -> {

                }
            }

        }


        viewModel.startDownloadLiveData.observe(viewLifecycleOwner) { properWidth ->
            startDownloading(properWidth)
        }

        viewModel.stopDownloadingLiveData.observe(viewLifecycleOwner) {
            stopDownloading()
        }


    }

    private fun navigateToNextDest() {
        val findNavController = findNavController()
        val graphInflater = findNavController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)



        navGraph.startDestination = R.id.quranPage

        findNavController.graph = navGraph
        findNavController.navigateSafely(
            R.id.quranPage,
            R.id.action_downloadFragment_to_quranPage
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}