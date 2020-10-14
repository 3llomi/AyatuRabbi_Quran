package com.devlomi.ayaturabbi.ui.quran_page

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.devlomi.ayaturabbi.ColorItem
import com.devlomi.ayaturabbi.ColorPickerListener
import com.devlomi.ayaturabbi.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.options_buttons_layout.*
import kotlinx.android.synthetic.main.options_panel_layout.*
import kotlinx.android.synthetic.main.quran_page_fragment.*


@AndroidEntryPoint
class QuranPageFragment : Fragment(R.layout.quran_page_fragment) {


    private lateinit var adapter: QuranPageAdapter
    private val viewModel: QuranPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QuranPageAdapter(viewLifecycleOwner, viewModel.useWhiteColorLiveData)
        adapter.adapterListener = object : AdapterListener {
            override fun onClick(pos: Int, quranPageItem: QuranPageItem) {
                options_menu_panel.isVisible = !options_menu_panel.isVisible

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
        viewModel.loadData()

        btn_color_picker.setOnClickListener {
            color_picker_layout.isVisible = !color_picker_layout.isVisible
        }
        color_picker_layout.colorPickerListener = object : ColorPickerListener {
            override fun onItemClick(colorItem: ColorItem) {
                viewModel.colorPicked(colorItem)
            }
        }

        // This callback will only be called when MyFragment is at least Started.
        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (options_menu_panel.isVisible) {
                        options_menu_panel.isVisible = false
                    } else {
                        Navigation.findNavController(view).navigateUp()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


    }

    private fun subscribeObservers() {
        viewModel.updateListLiveData.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.updateBackgroundColor.observe(viewLifecycleOwner, { backgroundColorRes ->
            quranPage_root.setBackgroundResource(backgroundColorRes)
        })

        viewModel.setCurrentIndexLiveData.observe(viewLifecycleOwner, { index ->
            viewpager.setCurrentItem(index, false)
        })
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }
}