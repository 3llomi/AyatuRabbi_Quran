package com.devlomi.ayaturabbi.ui.bookmarks

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.BundleConstants
import com.devlomi.ayaturabbi.databinding.BookmarksFragmentBinding
import com.devlomi.ayaturabbi.db.bookmark.Bookmark
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BookmarksFragment : Fragment(R.layout.bookmarks_fragment) {


    private val viewModel: BookmarksViewModel by viewModels()

    private lateinit var adapter: BookmarkAdapter
    private var _binding : BookmarksFragmentBinding? = null
    private val binding: BookmarksFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        subscribeObservers()
        viewModel.loadBookmarks()
    }


    private fun initAdapter() {
        adapter = BookmarkAdapter()
        adapter.adapterListener = object : AdapterListener {

            override fun onDeleteClick(position: Int, bookmark: Bookmark) {
                MaterialDialog(requireContext(), BottomSheet(LayoutMode.WRAP_CONTENT))

                    .show {
                        lifecycleOwner(viewLifecycleOwner)
                        title(R.string.delete_bookmark_confirmation)
                        message(R.string.delete_bookmark_message)
                        negativeButton(R.string.cancel)
                        positiveButton( R.string.yes) {
                            viewModel.onDeleteClick(bookmark)
                        }
                    }
            }

            override fun onItemClick(position: Int, bookmark: Bookmark) {
                findNavController().navigate(
                    R.id.action_bookmarksFragment_to_quranPage, bundleOf(
                        Pair(BundleConstants.PAGE_NUMBER_TAG, bookmark.pageNumber)
                    )
                )
            }
        }
        binding.rvBookmarks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBookmarks.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            adapter.submitList(bookmarks)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}