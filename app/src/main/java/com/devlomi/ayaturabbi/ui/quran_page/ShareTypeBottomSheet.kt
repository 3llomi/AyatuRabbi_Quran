package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.devlomi.ayaturabbi.BuildConfig
import com.devlomi.ayaturabbi.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.share_type_bottomsheet.*
import java.io.File


@AndroidEntryPoint
class ShareTypeBottomSheet : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.share_type_bottomsheet, container, false)
    }

    private val viewModel: QuranPageViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_text_type.setOnClickListener {
            viewModel.shareTypeChosen(ShareType.TEXT)

        }

        tv_image_type.setOnClickListener {
            viewModel.shareTypeChosen(ShareType.IMAGE)
        }

        subscribeObservers()


    }

    private fun subscribeObservers() {
        viewModel.shareText.observe(viewLifecycleOwner) { shareText ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }

            requireActivity().startActivity(intent)

            dismiss()
        }

        viewModel.shareImage.observe(viewLifecycleOwner) { imageFilePath ->


            val uri = Uri.fromFile(File(imageFilePath))
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

            val imageUri =
                FileProvider.getUriForFile(
                    requireContext(),
                    "${BuildConfig.APPLICATION_ID}.provider",
                    File(imageFilePath)
                )

            intent.putExtra(Intent.EXTRA_STREAM, imageUri)
            intent.type = "image/*"

            val chooser = Intent.createChooser(intent, "Share Using")

            val resInfoList: List<ResolveInfo> = requireContext().packageManager
                .queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY)

            for (resolveInfo in resInfoList) {
                val packageName = resolveInfo.activityInfo.packageName
                requireContext().grantUriPermission(
                    packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }

            requireActivity().startActivity(chooser)
            dismiss()
        }


    }


}


enum class ShareType {
    TEXT, IMAGE
}