package com.devlomi.shared.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devlomi.shared.common.removed
import com.devlomi.shared.data.db.bookmark.Bookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarksViewModel(
    private val bookmarkDao: com.devlomi.shared.data.db.bookmark.BookmarkDao
) : ViewModel() {


    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> get() = _state.asStateFlow()

    private val navigationChannel = Channel<BookmarkNavigationEvents>()
    val navigationEvents: Flow<BookmarkNavigationEvents> = navigationChannel.receiveAsFlow()

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
            is BookmarkEvents.DeleteDialogAction -> {
                when (event.action) {
                    is com.devlomi.shared.ui.suras.DialogActions.OnConfirm<*> -> onConfirmDeleteClick()
                    com.devlomi.shared.ui.suras.DialogActions.OnDismiss -> {
                        _state.update {
                            it.copy(
                                deleteBookmarkDialogState = it.deleteBookmarkDialogState.copy(
                                    isVisible = false
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun onBookmarkClick(bookmark: Bookmark) {
        viewModelScope.launch {
            navigationChannel.send(BookmarkNavigationEvents.ToQuranPageWithPageNumber(bookmark.pageNumber))
        }
    }

    fun onConfirmDeleteClick() {
        val bookmark = state.value.bookmarkToDelete ?: return
        _state.update {
            it.copy(
                deleteBookmarkDialogState = it.deleteBookmarkDialogState.copy(isVisible = false)
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                bookmarkDao.unBookmark(bookmark)
                withContext(Dispatchers.Main) {
                    _state.update {
                        it.copy(
                            bookmarks = it.bookmarks.removed(bookmark).toMutableList(),
                            bookmarkToDelete = null
                        )
                    }
                }
            } catch (e: Exception) {
                Logger.e("BookmarksViewModel Error deleting bookmark: ${e.message}")
            }
        }

    }

    fun onDeleteClick(bookmark: Bookmark) {
        _state.update {
            it.copy(
                bookmarkToDelete = bookmark,
                deleteBookmarkDialogState = it.deleteBookmarkDialogState.copy(isVisible = true)
            )
        }


    }


}