package com.devlomi.ayaturabbi.ui.search

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.util.KeyboardHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_card_search_view.*
import kotlinx.android.synthetic.main.search_fragment.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var adapter: SearchResultsAdapter

    private val viewModel: SearchViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setSearchEditTextProperties()
        subscribeObservers()


        et_search.doOnTextChanged { text, start, before, count ->
            viewModel.searchForAyah(text.toString())
        }
    }

    private fun setSearchEditTextProperties() {
        et_search.setRawInputType(InputType.TYPE_CLASS_TEXT);
        et_search.imeOptions = EditorInfo.IME_ACTION_SEARCH
        et_search.hint = getString(R.string.search_for_ayah)
    }

    private fun initAdapter() {
        adapter = SearchResultsAdapter { searchResult ->
            Log.d("3llomi", "search result page number ${searchResult.pageNumber} ")
            KeyboardHelper.hideSoftKeyboard(requireContext(), et_search)
            findNavController().navigate(
                R.id.action_searchFragment_to_quranPage,
                bundleOf(Pair(BundleConstants.PAGE_NUMBER_TAG, searchResult.pageNumber))
            )
        }
        rv_search_results.layoutManager = LinearLayoutManager(requireContext())
        rv_search_results.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            adapter.submitList(searchResults)
        }
    }
}