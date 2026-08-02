package com.devlomi.shared.ui.bookmark

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.ic_article
import ayaturabbi.shared.generated.resources.ic_bookmark
import ayaturabbi.shared.generated.resources.ic_clear
import ayaturabbi.shared.generated.resources.ic_note
import ayaturabbi.shared.generated.resources.ic_reading_quran
import com.devlomi.shared.ui.bookmark.BookmarksViewModel
import com.devlomi.shared.data.db.bookmark.Bookmark
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun BookmarksScreen(
//    onOpenBookmark: (pageNumber: Int) -> Unit,
    state: BookmarkState,
    onEvent: (BookmarkEvents) -> Unit
) {
    var pendingDelete by remember { mutableStateOf<com.devlomi.shared.data.db.bookmark.Bookmark?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            BookmarksTitle(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(state.bookmarks, key = { it.timestamp }) { bookmark ->
                    BookmarkItem(
                        bookmark = bookmark,
                        onClick = {
                            onEvent(BookmarkEvents.OnClick(bookmark))
                        },
                        onDeleteClick = {
                            pendingDelete = bookmark
                        }
                    )
                }
            }
        }

        if (pendingDelete != null) {
            AlertDialog(
                onDismissRequest = { pendingDelete = null },
                title = { Text("Delete bookmark?") },
                text = { Text("Are you sure you want to delete this bookmark?") },
                dismissButton = {
                    TextButton(onClick = { pendingDelete = null }) { Text("Cancel") }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            pendingDelete?.let{BookmarkEvents.OnDelete(it)}
                            pendingDelete = null
                        }
                    ) { Text("Yes") }
                }
            )
        }
    }
}

@Composable
private fun BookmarksTitle(modifier: Modifier = Modifier) {
    // XML: centered title + drawableRight bookmark icon + 8dp gap
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Saved Bookmarks",
//            color = BookmarksUiTokens.titleColor,
//            style = BookmarksUiTokens.titleTextStyle,
            textAlign = TextAlign.Center
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        Icon(
            imageVector = vectorResource(Res.drawable.ic_bookmark),
            contentDescription = null,
//            tint = BookmarksUiTokens.titleColor
        )
    }
}

@Composable
private fun BookmarkItem(
    bookmark: com.devlomi.shared.data.db.bookmark.Bookmark,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // XML root: selectable + clickable + 8dp top/bottom
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        // tv_surah_name: right aligned, right icon, marginRight 16
        TrailingIconTextRow(
            text = bookmark.surahName,
            icon = Res.drawable.ic_reading_quran,
//            textStyle = BookmarksUiTokens.surahTextStyle,
//            tint = BookmarksUiTokens.primaryTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        )

        // tv_page_number: top 8, end 16, right icon
        TrailingIconTextRow(
            text = bookmark.pageNumber.toString(),
            icon = Res.drawable.ic_article,
//            textStyle = BookmarksUiTokens.regularTextStyle,
//            tint = BookmarksUiTokens.secondaryTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp)
        )

        // tv_note: top 8, end 16, right icon
        TrailingIconTextRow(
            text = bookmark.note?.takeIf { it.isNotBlank() } ?: "لا يوجد",
            icon = Res.drawable.ic_note,
//            textStyle = BookmarksUiTokens.regularTextStyle,
//            tint = BookmarksUiTokens.primaryTextColor,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp)
        )

        // tv_date: start 16, aligned left near top block
        Text(
            text = bookmark.timestamp.toString(),//TODO GET CREATED AT INSTEAD
//            color = BookmarksUiTokens.primaryTextColor,
//            style = BookmarksUiTokens.regularTextStyle,
            modifier = Modifier.padding(start = 16.dp)
        )

        // btn_delete_bookmark: start/end 32, top 16, centered
        Button(
            onClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 16.dp),
//            colors = BookmarksUiTokens.deleteButtonColors
        ) {
            Text(
                text = "Delete",
//                color = BookmarksUiTokens.deleteButtonTextColor,
//                style = BookmarksUiTokens.regularTextStyle
            )
            androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
            Icon(
                imageVector = vectorResource(Res.drawable.ic_clear),
                contentDescription = null,
//                tint = BookmarksUiTokens.deleteIconTint
            )
        }
    }
}

@Composable
private fun TrailingIconTextRow(
    text: String,
    icon: DrawableResource,
//    textStyle: androidx.compose.ui.text.TextStyle,
//    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text,
//            color = tint, style = textStyle
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        Icon(
            imageVector = vectorResource(icon),
            contentDescription = null,
//            tint = tint
        )
    }
}