package com.devlomi.ayaturabbi.ui.quran_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.devlomi.ayaturabbi.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.quran_page_fragment.*

@AndroidEntryPoint
class QuranPageFragment : Fragment(R.layout.quran_page_fragment) {


    private lateinit var adapter: QuranPageAdapter
    private val viewModel: QuranPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QuranPageAdapter()
        viewpager.adapter = adapter

        subscribeObservers()

        viewModel.loadData()

    }

    private fun subscribeObservers() {
        viewModel.updateListLiveData.observe(viewLifecycleOwner, {
            adapter.setItems(it)
        })
    }
}