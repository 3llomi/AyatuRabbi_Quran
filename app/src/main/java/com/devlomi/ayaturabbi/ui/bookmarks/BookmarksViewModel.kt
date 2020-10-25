package com.devlomi.ayaturabbi.ui.bookmarks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.ayaturabbi.db.bookmark.Bookmark
import com.devlomi.ayaturabbi.db.bookmark.BookmarkDao
import com.devlomi.ayaturabbi.extensions.removed
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel @ViewModelInject constructor(
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _bookmarks = MutableLiveData<MutableList<Bookmark>>()
    val bookmarks: LiveData<MutableList<Bookmark>> get() = _bookmarks

    fun loadBookmarks() {
        viewModelScope.launch(IO) {
            val allBookmarks =
                bookmarkDao.getAllBookmarks().sortedByDescending { it.timestamp }
            withContext(Main) {
                _bookmarks.value = allBookmarks.toMutableList()
            }
        }
    }

    fun onDeleteClick(bookmark: Bookmark) {
        viewModelScope.launch(IO) {
            bookmarkDao.unBookmark(bookmark)
            withContext(Main) {
                _bookmarks.value = _bookmarks.value?.removed(bookmark)
            }
        }
    }


}