package com.devlomi.shared.ui.bookmark

import com.devlomi.shared.data.db.bookmark.Bookmark

sealed class BookmarkEvents {
    data class OnDelete(val bookmark: com.devlomi.shared.data.db.bookmark.Bookmark): BookmarkEvents()
    data class OnClick(val bookmark: com.devlomi.shared.data.db.bookmark.Bookmark): BookmarkEvents()
}