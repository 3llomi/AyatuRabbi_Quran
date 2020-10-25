package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.devlomi.ayaturabbi.BuildConfig
import com.devlomi.ayaturabbi.ColorItem
import com.devlomi.ayaturabbi.ColorPickerListener
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfo
import com.devlomi.ayaturabbi.extensions.getIntOrNull
import com.devlomi.ayaturabbi.ui.main.MainViewModel
import com.warkiz.tickseekbar.OnSeekChangeListener
import com.warkiz.tickseekbar.SeekParams
import com.warkiz.tickseekbar.TickSeekBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.options_buttons_layout.*
import kotlinx.android.synthetic.main.options_panel_layout.*
import kotlinx.android.synthetic.main.quran_page_fragment.*
import kotlinx.android.synthetic.main.quran_page_fragment.quranPage_root
import kotlinx.android.synthetic.main.quran_page_fragment.viewpager
import java.io.File


@AndroidEntryPoint
class QuranPageFragment : Fragment(R.layout.quran_page_fragment), OnSeekChangeListener {

    private val ANIMATION_DURATION: Long = 250


    private lateinit var adapter: QuranPageAdapter

    private val viewModel: QuranPageViewModel by viewModels()


    private lateinit var mainViewModel: MainViewModel
    private var isOptionPanelLayoutHidden = true
    private var isColorPanelHidden = true
    private var optionsPanelHeight = 0
    private var colorPickerHeight = 0

    var surahNumber: Int? = null
    var pageNumber: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("3llomi", "onCreate")
        surahNumber = arguments?.getIntOrNull(BundleConstants.SURAH_NUMBER_TAG)
        pageNumber = arguments?.getIntOrNull(BundleConstants.PAGE_NUMBER_TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        adapter = QuranPageAdapter(viewLifecycleOwner, viewModel.useWhiteColor)
        adapter.pageScale = viewModel.pageScale
        adapter.adapterListener = object : AdapterListener {
            override fun onClick(pos: Int, quranPageItem: QuranPageItem) {
                isOptionPanelLayoutHidden = !isOptionPanelLayoutHidden
                hideOrShowPanelLayout(!isOptionPanelLayoutHidden)
            }
        }

        viewpager.adapter = adapter
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.onPageChanged(position)
            }
        })

        subscribeObservers()


        viewModel.loadData(surahNumber, pageNumber)

        //when going back from Fragment
        // bundle extras would still have value, therefore we need to clear these out after loading data
        clearArguments()
        btn_color_picker.setOnClickListener {
            isColorPanelHidden = !isColorPanelHidden
            hideOrShowColorLayout(!isColorPanelHidden)
        }

        color_picker_layout.colorPickerListener = object : ColorPickerListener {
            override fun onItemClick(colorItem: ColorItem) {
                viewModel.colorPicked(colorItem)
            }
        }

        btn_suras.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_surasFragment)
        }

        btn_bookmark.setOnClickListener {
            viewModel.bookmarkClicked()
        }

        btn_bookmark.setOnLongClickListener {
            showBookmarkNoteDialog()
            true
        }

        btn_bookmarked_pages.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_bookmarksFragment)
        }

        search_layout.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_searchFragment)
        }

        btn_share.setOnClickListener {

            showShareDialog()
        }

        btn_settings.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_settingsFragment)
        }

        btn_zoom.setOnClickListener {
            viewModel.btnZoomClicked()

        }


        registerBackPressedCallback()


        options_menu_panel.doOnPreDraw { view ->
            val height = view.height

            if (isOptionPanelLayoutHidden) {
                view.translationY = height.toFloat()
            }

            if (isColorPanelHidden) {
                color_picker_layout.translationY = height.toFloat()
                color_picker_layout.isVisible = false
            } else {
                setBtnColorFilter()
            }

            colorPickerHeight = height
            optionsPanelHeight = height
        }

    }

    private fun showZoomDialog(initialProgress: Int) {

        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {


            customView(R.layout.zoom_options)

            val seekBar = this.findViewById<TickSeekBar>(R.id.seekbar)
            seekBar.setProgress(initialProgress.toFloat())
            seekBar.onSeekChangeListener = this@QuranPageFragment
//                object : OnSeekChangeListener {
//                override fun onSeeking(seekParams: SeekParams) {
//                    Log.d("3llomi", "onScaling ")
//                    viewModel.setPageScale(seekParams.thumbPosition)
//                }
//
//                override fun onStartTrackingTouch(seekBar: TickSeekBar?) = Unit
//
//                override fun onStopTrackingTouch(seekBar: TickSeekBar) = Unit
//            }

            onDismiss {
                viewModel.zoomDone()
            }
        }

    }

    private fun showBookmarkNoteDialog() {
        val editText = EditText(context)
        editText.hint = getString(R.string.note)
        MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            lifecycleOwner(viewLifecycleOwner)
            title(R.string.add_note)
            customView(view = editText)
            positiveButton(R.string.save) {
                viewModel.bookmarkWithNote(editText.text.toString())
            }
            negativeButton(R.string.cancel) {}
            onDismiss {
                mainViewModel.hideUI()
            }
        }
    }

    private fun showShareDialog() {
        MaterialDialog(requireActivity(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            lifecycleOwner(viewLifecycleOwner)
            customView(R.layout.share_type_bottomsheet)

            val text = this.findViewById<TextView>(R.id.tv_text_type)
            val image = this.findViewById<TextView>(R.id.tv_image_type)

            text.setOnClickListener {
                dismiss()
                viewModel.shareTypeChosen(ShareType.TEXT)

            }

            image.setOnClickListener {
                dismiss()
                viewModel.shareTypeChosen(ShareType.IMAGE)
            }

        }
    }

    private fun registerBackPressedCallback() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (!isColorPanelHidden) {
                        isColorPanelHidden = !isColorPanelHidden
                        hideOrShowColorLayout(!isColorPanelHidden)
                    } else if (!isOptionPanelLayoutHidden) {
                        isOptionPanelLayoutHidden = !isOptionPanelLayoutHidden
                        hideOrShowPanelLayout(!isOptionPanelLayoutHidden)
                    } else {
                        activity?.finish()
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun clearArguments() {
        surahNumber = null
        pageNumber = null
    }


    private fun subscribeObservers() {
        viewModel.quranPages.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.backgroundColor.observe(viewLifecycleOwner) { backgroundColorRes ->
            quranPage_root.setBackgroundResource(backgroundColorRes)
        }

        viewModel.currentIndex.observe(viewLifecycleOwner) { index ->
            if (index != viewpager.currentItem) {
                viewpager.setCurrentItem(index, false)
            }
        }

        viewModel.isBookmarked.observe(viewLifecycleOwner) { isBookmarked ->
            val drawable =
                if (isBookmarked) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
            btn_bookmark.setImageResource(drawable)
        }

        viewModel.shareText.observe(viewLifecycleOwner) {
            it?.let { shareText ->

                shareText(shareText)
                viewModel.shareDone()
            }
        }

        viewModel.shareImage.observe(viewLifecycleOwner) {
            it?.let { imageFilePath ->
                shareImage(imageFilePath)
                viewModel.shareDone()
            }
        }

        viewModel.showZoomSheet.observe(viewLifecycleOwner) {
            it?.let { progress ->
                showZoomDialog(progress)
            }

        }


    }

    private fun shareText(shareText: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }

        requireActivity().startActivity(intent)
    }

    private fun shareImage(imageFilePath: String) {
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

        val chooser = Intent.createChooser(intent, getString(R.string.share_using)).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

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
    }


    private fun hideOrShowPanelLayout(hide: Boolean) {

        if (hide) {
            options_menu_panel.animate().translationY(0f).duration = ANIMATION_DURATION
        } else {
            options_menu_panel.animate().translationY(optionsPanelHeight.toFloat()).duration =
                ANIMATION_DURATION
        }


    }

    private fun hideOrShowColorLayout(show: Boolean) {

        if (show) {
            setBtnColorFilter()

            color_picker_layout.isVisible = true
            color_picker_layout.animate().translationY(0f).duration = ANIMATION_DURATION
        } else {
            btn_color_picker.clearColorFilter()
            color_picker_layout.animate().translationY(optionsPanelHeight.toFloat()).duration =
                ANIMATION_DURATION
            color_picker_layout.isVisible = false

        }


    }

    private fun setBtnColorFilter() {
        btn_color_picker.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorSecondary
            ), PorterDuff.Mode.SRC_IN
        )
    }


    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    override fun onSeeking(seekParams: SeekParams) {
        viewModel.setPageScale(seekParams.thumbPosition)
    }

    override fun onStartTrackingTouch(seekBar: TickSeekBar?) = Unit

    override fun onStopTrackingTouch(seekBar: TickSeekBar?) = Unit
}
