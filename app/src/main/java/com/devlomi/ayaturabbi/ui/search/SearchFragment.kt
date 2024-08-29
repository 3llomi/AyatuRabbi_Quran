package com.devlomi.ayaturabbi.ui.search

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.databinding.SearchCardSearchViewBinding
import com.devlomi.ayaturabbi.databinding.SearchFragmentBinding
import com.devlomi.ayaturabbi.util.KeyboardHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var adapter: SearchResultsAdapter

    private val viewModel: SearchViewModel by viewModels()

    private var _binding: SearchFragmentBinding? = null
    private val binding: SearchFragmentBinding get() = _binding!!

    private var _searchCardBinding : SearchCardSearchViewBinding? = null
    private val searchCardBinding: SearchCardSearchViewBinding get() = _searchCardBinding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        _searchCardBinding = SearchCardSearchViewBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        setSearchEditTextProperties()
        subscribeObservers()


        searchCardBinding.etSearch.doOnTextChanged { text, start, before, count ->
            viewModel.searchForAyah(text.toString())
        }
    }

    private fun setSearchEditTextProperties() {
        searchCardBinding.etSearch.setRawInputType(InputType.TYPE_CLASS_TEXT);
        searchCardBinding.etSearch.imeOptions = EditorInfo.IME_ACTION_SEARCH
        searchCardBinding.etSearch.hint = getString(R.string.search_for_ayah)
    }

    private fun initAdapter() {
        adapter = SearchResultsAdapter { searchResult ->
            KeyboardHelper.hideSoftKeyboard(requireContext(), searchCardBinding.etSearch)
            findNavController().navigate(
                R.id.action_searchFragment_to_quranPage,
                bundleOf(Pair(BundleConstants.PAGE_NUMBER_TAG, searchResult.pageNumber))
            )
        }
        binding.rvSearchResults.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResults.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner) { searchResults ->
            binding.imgQuran.isVisible = searchResults.isEmpty()
            adapter.submitList(searchResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchCardBinding = null
    }
}