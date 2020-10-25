package com.devlomi.ayaturabbi.ui.suras

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.util.KeyboardHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_card_layout.*
import kotlinx.android.synthetic.main.search_card_search_view.*
import kotlinx.android.synthetic.main.suras_fragment.*
import me.zhanghai.android.systemuihelper.SystemUiHelper

@AndroidEntryPoint
class SurasFragment : Fragment(R.layout.suras_fragment) {


    private val viewModel: SurasViewModel by viewModels()

    private lateinit var adapter: SurahAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()


        subscribeObservers()
        viewModel.loadData()

        et_search.doOnTextChanged { text, start, before, count ->
            viewModel.searchForSura(text.toString())
        }

        btn_go_to_page.setOnClickListener {
            val editText = EditText(requireContext())

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.hint = getString(R.string.page_number)
            MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {

                noAutoDismiss()
                lifecycleOwner(viewLifecycleOwner)
                customView(view = editText)
                positiveButton(R.string.go) {
                    val page = editText.text.toString()
                    if (viewModel.isPageNumberValid(page)) {
                        findNavController().navigate(
                            R.id.action_surasFragment_to_quranPage,
                            bundleOf(Pair(BundleConstants.PAGE_NUMBER_TAG, page.toInt()))
                        )
                    }else{
                        editText.error = getString(R.string.invalid_page)
                    }
                }
                negativeButton(R.string.cancel){dismiss()}
            }

        }

    }

    private fun initAdapter() {
        adapter = SurahAdapter { surah ->
            KeyboardHelper.hideSoftKeyboard(requireContext(), et_search)
            findNavController().navigate(
                R.id.action_surasFragment_to_quranPage,
                bundleOf(Pair(BundleConstants.SURAH_NUMBER_TAG, surah.surahNumber))
            )
        }
        rv_suras.layoutManager = LinearLayoutManager(requireContext())
        rv_suras.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.suras.observe(viewLifecycleOwner) { suras ->
            adapter.submitList(suras)
        }

        viewModel.filterdSuras.observe(viewLifecycleOwner) { filteredSuras ->
            adapter.submitList(filteredSuras)
        }
    }
}