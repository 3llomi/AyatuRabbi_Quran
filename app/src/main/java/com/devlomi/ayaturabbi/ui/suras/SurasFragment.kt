package com.devlomi.ayaturabbi.ui.suras

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.databinding.SearchCardSearchViewBinding
import com.devlomi.ayaturabbi.databinding.SurasFragmentBinding
import com.devlomi.ayaturabbi.util.KeyboardHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SurasFragment : Fragment(R.layout.suras_fragment) {


    private val viewModel: SurasViewModel by viewModels()

    private lateinit var adapter: SurahAdapter

    private var _binding : SurasFragmentBinding? = null
    private val binding: SurasFragmentBinding get() = _binding!!
    private var _searchCardBinding : SearchCardSearchViewBinding? = null
    private val searchCardBinding: SearchCardSearchViewBinding get() = _searchCardBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SurasFragmentBinding.inflate(inflater, container, false)
        _searchCardBinding = SearchCardSearchViewBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()


        subscribeObservers()
        viewModel.loadData()

        searchCardBinding.etSearch.doOnTextChanged { text, start, before, count ->
            viewModel.searchForSura(text.toString())
        }

        binding.btnGoToPage.setOnClickListener {
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
                    } else {
                        editText.error = getString(R.string.invalid_page)
                    }
                }
                negativeButton(R.string.cancel) { dismiss() }
            }

        }

        binding.btnGoToJuzoa.setOnClickListener {
            val editText = EditText(requireContext())

            editText.inputType = InputType.TYPE_CLASS_NUMBER
            editText.hint = getString(R.string.juzoa_number)
            MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT)).show {

                noAutoDismiss()
                lifecycleOwner(viewLifecycleOwner)
                customView(view = editText)
                positiveButton(R.string.go) {
                    val juzoa = editText.text.toString()
                    val pageNumber = viewModel.getPageNumberByJuzoaIfValid(juzoa)
                    if (pageNumber != null) {
                        findNavController().navigate(
                            R.id.action_surasFragment_to_quranPage,
                            bundleOf(Pair(BundleConstants.PAGE_NUMBER_TAG, pageNumber))
                        )
                    } else {
                        editText.error = getString(R.string.invalid_juzoa)
                    }
                }
                negativeButton(R.string.cancel) { dismiss() }
            }

        }

    }

    private fun initAdapter() {
        adapter = SurahAdapter { surah ->
            KeyboardHelper.hideSoftKeyboard(requireContext(), searchCardBinding.etSearch)
            findNavController().navigate(
                R.id.action_surasFragment_to_quranPage,
                bundleOf(Pair(BundleConstants.SURAH_NUMBER_TAG, surah.surahNumber))
            )
        }
        binding.rvSuras.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSuras.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.suras.observe(viewLifecycleOwner) { suras ->
            adapter.submitList(suras)
        }

        viewModel.filterdSuras.observe(viewLifecycleOwner) { filteredSuras ->
            adapter.submitList(filteredSuras)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _searchCardBinding = null
    }
}