package com.devlomi.shared

import androidx.lifecycle.viewModelScope
import com.devlomi.shared.db.bookmark.Bookmark
import com.devlomi.shared.db.bookmark.BookmarkDao
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel (
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(listOf())
    @NativeCoroutinesState
    val bookmarks: StateFlow<List<Bookmark>> get() = _bookmarks.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allBookmarks =
                    bookmarkDao.getAllBookmarks().sortedByDescending { it.timestamp }
                withContext(Main) {
                    _bookmarks.value = allBookmarks.toMutableList()
                }
            } catch (e: Exception) {

            }
        }

    }

    fun onDeleteClick(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookmarkDao.unBookmark(bookmark)
                withContext(Main) {
                    _bookmarks.value = _bookmarks.value.removed(bookmark)
                }
            } catch (e: Exception) {

            }
        }

    }


}
