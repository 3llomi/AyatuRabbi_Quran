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
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.cancel
import ayaturabbi.shared.generated.resources.delete
import ayaturabbi.shared.generated.resources.delete_bookmark_confirmation
import ayaturabbi.shared.generated.resources.delete_bookmark_message
import ayaturabbi.shared.generated.resources.ic_article
import ayaturabbi.shared.generated.resources.ic_bookmark
import ayaturabbi.shared.generated.resources.ic_clear
import ayaturabbi.shared.generated.resources.ic_note
import ayaturabbi.shared.generated.resources.ic_reading_quran
import ayaturabbi.shared.generated.resources.saved_bookmarks
import ayaturabbi.shared.generated.resources.yes
import com.devlomi.shared.common.DateFormatter
import com.devlomi.shared.data.db.bookmark.Bookmark
import com.devlomi.shared.ui.suras.DialogActions
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun BookmarksScreen(
//    onOpenBookmark: (pageNumber: Int) -> Unit,
    state: BookmarkState,
    onEvent: (BookmarkEvents) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
                            onEvent(BookmarkEvents.OnDelete(bookmark))
                        }
                    )
                }
            }
        }

        if (state.deleteBookmarkDialogState.isVisible) {
            AlertDialog(
                onDismissRequest = { onEvent(BookmarkEvents.DeleteDialogAction(DialogActions.OnDismiss)) },
                title = { Text(stringResource(Res.string.delete_bookmark_confirmation)) },
                text = { Text(stringResource(Res.string.delete_bookmark_message)) },
                dismissButton = {
                    TextButton(
                        onClick = { onEvent(BookmarkEvents.DeleteDialogAction(DialogActions.OnDismiss)) },
                        colors = textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                    ) { Text(stringResource(Res.string.cancel)) }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(BookmarkEvents.DeleteDialogAction(DialogActions.OnConfirm(null)))
                        },
                        colors = textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                    ) { Text(stringResource(Res.string.yes)) }
                }
            )
        }
    }
}

@Composable
private fun BookmarksTitle(modifier: Modifier = Modifier) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.saved_bookmarks),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        Icon(
            imageVector = vectorResource(Res.drawable.ic_bookmark),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun BookmarkItem(
    bookmark: Bookmark,
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
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
        )

        // tv_page_number: top 8, end 16, right icon
        TrailingIconTextRow(
            text = bookmark.pageNumber.toString(),
            icon = Res.drawable.ic_article,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp)
        )

        // tv_note: top 8, end 16, right icon
        TrailingIconTextRow(
            text = bookmark.note ?: "",
            icon = Res.drawable.ic_note,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 16.dp)
        )

        // tv_date: start 16, aligned left near top block
        Text(
            text = DateFormatter.formatDate(bookmark.timestamp),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp)
        )

        // btn_delete_bookmark: start/end 32, top 16, centered
        Button(
            onClick = onDeleteClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 16.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(
                text = stringResource(Res.string.delete),
                style = MaterialTheme.typography.bodyMedium
            )
            androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
            Icon(
                imageVector = vectorResource(Res.drawable.ic_clear),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onError
            )
        }
    }
}

@Composable
private fun TrailingIconTextRow(
    text: String,
    icon: DrawableResource,
    tint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, color = tint, style = MaterialTheme.typography.bodyMedium)
        androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
        Icon(
            imageVector = vectorResource(icon),
            contentDescription = null,
            tint = tint
        )
    }
}