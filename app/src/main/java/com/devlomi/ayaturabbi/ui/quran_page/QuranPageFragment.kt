package com.devlomi.ayaturabbi.ui.quran_page

import android.animation.Animator
import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.devlomi.ayaturabbi.ColorItem
import com.devlomi.ayaturabbi.ColorPickerListener
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfo
import com.devlomi.ayaturabbi.db.ayahinfo.ImageAyahUtils
import com.devlomi.ayaturabbi.extensions.getIntOrNull
import com.devlomi.ayaturabbi.ui.main.MainActivity
import com.devlomi.ayaturabbi.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.item_quran_page.*
import kotlinx.android.synthetic.main.options_buttons_layout.*
import kotlinx.android.synthetic.main.options_panel_layout.*
import kotlinx.android.synthetic.main.quran_page_fragment.*
import kotlinx.android.synthetic.main.quran_page_fragment.quranPage_root
import kotlinx.android.synthetic.main.quran_page_fragment.viewpager


@AndroidEntryPoint
class QuranPageFragment : Fragment(R.layout.quran_page_fragment) {

    private val ANIMATION_DURATION: Long = 250


    private lateinit var adapter: QuranPageAdapter

    private val viewModel: QuranPageViewModel by viewModels()


    private var coordinates: Map<String, MutableList<AyahInfo>?>? = null

    private var isOptionPanelLayoutHidden = true
    private var isColorPanelHidden = true
    private var optionsPanelHeight = 0
    private var colorPickerHeight = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        Log.d("3llomi", "onViewCreated ")

        adapter = QuranPageAdapter(viewLifecycleOwner, viewModel.useWhiteColor)
        adapter.adapterListener = object : AdapterListener {
            override fun onClick(pos: Int, quranPageItem: QuranPageItem) {

                isOptionPanelLayoutHidden = !isOptionPanelLayoutHidden
//                viewModel.isOptionPanelLayoutHidden = !isOptionPanelLayoutHidden
                hideOrShowPanelLayout(!isOptionPanelLayoutHidden)

            }

            override fun onLongClick(
                pos: Int,
                quranPageItem: QuranPageItem,
                motionEvent: MotionEvent
            ) {
//                Log.d("3llomi", "onLongClick $pos $motionEvent")
                coordinates?.let { coordinates ->
                    val ayahFromCoordinates = ImageAyahUtils.getAyahFromCoordinates(
                        coordinates,
                        img_quran,
                        motionEvent.x,
                        motionEvent.y
                    )
                    Log.d("3llomi", "ayahFromCoordinates $ayahFromCoordinates")
                }

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

        val surahNumber = arguments?.getIntOrNull(BundleConstants.SURAH_NUMBER_TAG)
        val pageNumber = arguments?.getIntOrNull(BundleConstants.PAGE_NUMBER_TAG)

        viewModel.loadData(surahNumber, pageNumber)

        btn_color_picker.setOnClickListener {
            isColorPanelHidden = !isColorPanelHidden
            hideOrShowColorLayout(!isColorPanelHidden)
        }

        color_picker_layout.colorPickerListener = object : ColorPickerListener {
            override fun onItemClick(colorItem: ColorItem) {
                viewModel.colorPicked(colorItem)
            }
        }

        btn_fahras.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_surasFragment)
        }

        btn_bookmark.setOnClickListener {
            viewModel.bookmarkClicked()
        }

        btn_bookmark.setOnLongClickListener {
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

            true
        }

        btn_bookmarked_pages.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_bookmarksFragment)
        }

        search_layout.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_searchFragment)
        }

        btn_share.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_shareTypeBottomSheet)
        }

        btn_settings.setOnClickListener {
            findNavController().navigate(R.id.action_quranPage_to_settingsFragment)
        }

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(false /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (!isColorPanelHidden) {
                        isColorPanelHidden = !isColorPanelHidden
                        hideOrShowColorLayout(!isColorPanelHidden)
                    } else if (!isOptionPanelLayoutHidden) {
                        isOptionPanelLayoutHidden = !isOptionPanelLayoutHidden
                        hideOrShowPanelLayout(!isOptionPanelLayoutHidden)
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


//        GlobalScope.launch(Dispatchers.IO) {
//            val ayahCoordinates = ayahInfoRepository.getVersesBoundsForPage(10).ayahCoordinates
//            this@QuranPageFragment.coordinates = ayahCoordinates
////            delay(1000)
//
////            withContext(Dispatchers.Main) {
////                val ayahFromCoordinates = ImageAyahUtils.getAyahFromCoordinates(
////                    ayahCoordinates,
////                    img_quran,
////                    img_quran.width / 2f,
////                    img_quran.height / 2f
////                )
////                Log.d("3llomi", "found sura ayah $ayahFromCoordinates")
////
////            }
//        }

        options_menu_panel.doOnPreDraw { view ->
            Log.d("3llomi", "onPreDraw")
            val height = view.height
            Log.d("3llomi", "optionsPanelHeight 0")

            if (isOptionPanelLayoutHidden) {
                view.translationY = height.toFloat()
            }

            if (isColorPanelHidden) {
                color_picker_layout.translationY = height.toFloat()
                color_picker_layout.isVisible = false
            }

            colorPickerHeight = height
            optionsPanelHeight = height
        }

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


    }


    private fun hideOrShowPanelLayout(hide: Boolean) {

        if (hide) {
            options_menu_panel.animate().translationY(0f).duration = ANIMATION_DURATION
        } else {
            options_menu_panel.animate().translationY(optionsPanelHeight.toFloat()).duration =
                ANIMATION_DURATION
        }


    }

    private fun hideOrShowColorLayout(hide: Boolean) {

        if (hide) {
            color_picker_layout.isVisible = true
            color_picker_layout.animate().translationY(0f).duration = ANIMATION_DURATION
        } else {
            color_picker_layout.animate().translationY(optionsPanelHeight.toFloat()).duration =
                ANIMATION_DURATION
            color_picker_layout.isVisible = false

        }


    }



    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }
}
