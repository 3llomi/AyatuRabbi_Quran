package com.devlomi.shared.ui.bookmark

import com.devlomi.shared.data.db.bookmark.Bookmark
import com.devlomi.shared.ui.suras.DialogActions

sealed class BookmarkEvents {
    object OnBackClick : BookmarkEvents()
    data class OnDelete(val bookmark: com.devlomi.shared.data.db.bookmark.Bookmark): BookmarkEvents()
    data class OnClick(val bookmark: com.devlomi.shared.data.db.bookmark.Bookmark): BookmarkEvents()
    data class DeleteDialogAction(val action: DialogActions): BookmarkEvents()
}