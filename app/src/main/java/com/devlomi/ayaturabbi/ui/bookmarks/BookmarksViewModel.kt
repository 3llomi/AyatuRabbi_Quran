package com.devlomi.ayaturabbi.ui.bookmarks


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.ayaturabbi.db.bookmark.Bookmark
import com.devlomi.ayaturabbi.db.bookmark.BookmarkDao
import com.devlomi.ayaturabbi.extensions.removed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val bookmarkDao: BookmarkDao
) : ViewModel() {

    private val _bookmarks = MutableLiveData<MutableList<Bookmark>>()
    val bookmarks: LiveData<MutableList<Bookmark>> get() = _bookmarks

    fun loadBookmarks() {
        viewModelScope.launch(IO) {
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
        viewModelScope.launch(IO) {
            try {
                bookmarkDao.unBookmark(bookmark)
                withContext(Main) {
                    _bookmarks.value = _bookmarks.value?.removed(bookmark)
                }
            } catch (e: Exception) {

            }
        }

    }


}