package com.devlomi.shared.ui.bookmark

import com.devlomi.shared.data.db.bookmark.Bookmark
import com.devlomi.shared.ui.components.DialogState

data class BookmarkState(
    val bookmarks:List<Bookmark> = listOf(),
    val bookmarkToDelete: Bookmark? = null,
    val deleteBookmarkDialogState: DialogState = DialogState()
    ) {

}