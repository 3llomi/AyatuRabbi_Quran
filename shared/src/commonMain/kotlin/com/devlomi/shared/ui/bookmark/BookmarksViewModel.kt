package com.devlomi.shared.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.common.removed
import com.devlomi.shared.data.db.bookmark.Bookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel(
    private val bookmarkDao: com.devlomi.shared.data.db.bookmark.BookmarkDao
) : ViewModel() {


    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> get() = _state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val allBookmarks =
                    bookmarkDao.getAllBookmarks().sortedByDescending { it.timestamp }
                withContext(Dispatchers.Main) {
                    _state.update { it.copy(bookmarks = allBookmarks.toMutableList()) }
                }
            } catch (e: Exception) {
            }
        }
    }
    fun onEvent(event: BookmarkEvents) {
        when (event) {
            is BookmarkEvents.OnDelete -> onDeleteClick(event.bookmark)
            is BookmarkEvents.OnClick -> onBookmarkClick(event.bookmark)
        }
    }

    private fun onBookmarkClick(bookmark: Bookmark) {
        //TODO?
    }

    fun onDeleteClick(bookmark: Bookmark) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookmarkDao.unBookmark(bookmark)
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            bookmarks = it.bookmarks.removed(bookmark).toMutableList()
                        )
                    }
                }
            } catch (e: Exception) {

            }
        }

    }


}