package com.devlomi.ayaturabbi.ui.suras

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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




//        view.requestLayout()


        subscribeObservers()
        viewModel.loadData()

        et_search.doOnTextChanged { text, start, before, count ->
            viewModel.searchForSura(text.toString())
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