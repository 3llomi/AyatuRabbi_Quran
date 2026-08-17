package com.devlomi.shared.ui.quran_page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import co.touchlab.kermit.Logger
import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.domain.ShareType
import com.devlomi.shared.domain.WhiteColorFilter
import com.devlomi.shared.domain.model.QuranPageItem
import com.devlomi.shared.ui.components.SearchCard
import com.devlomi.shared.ui.suras.DialogActions
import com.devlomi.shared.ui.suras.DialogActionsWithQuery
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import org.jetbrains.compose.resources.decodeToImageBitmap
import androidx.compose.material.icons.filled.Image
import androidx.compose.ui.text.font.FontFamily
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.add_note
import ayaturabbi.shared.generated.resources.aljuzoa
import ayaturabbi.shared.generated.resources.cancel
import ayaturabbi.shared.generated.resources.choose_share_type
import ayaturabbi.shared.generated.resources.image
import ayaturabbi.shared.generated.resources.note
import ayaturabbi.shared.generated.resources.naskh
import ayaturabbi.shared.generated.resources.save
import ayaturabbi.shared.generated.resources.search
import ayaturabbi.shared.generated.resources.surah
import ayaturabbi.shared.generated.resources.text
import com.devlomi.shared.common.asComposeColor
import com.devlomi.shared.ui.components.AndroidBackHandler
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranPageScreen(
    state: QuranPageState,
    onEvent: (QuranPageEvents) -> Unit
) {

    LifecycleEventEffect(Lifecycle.Event.ON_STOP) {
        onEvent(QuranPageEvents.OnStop)
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { state.quranPages.size }
    )

    LaunchedEffect(state.currentIndex, state.quranPages.size) {
        if (state.quranPages.isNotEmpty() && state.currentIndex != pagerState.currentPage) {
            pagerState.scrollToPage(state.currentIndex)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        if (state.quranPages.isNotEmpty() && pagerState.currentPage != state.currentIndex) {
            onEvent(QuranPageEvents.OnPageSwipe(pagerState.currentPage))
        }
    }

    val zoomBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    AndroidBackHandler {
        if (state.showZoomSheet){
            onEvent(QuranPageEvents.OnZoomDone)
        }else if(state.showColorsPanel){
            onEvent(QuranPageEvents.OnColorClick)
        }else if(state.showOptionsPanel){
            onEvent(QuranPageEvents.OnPageClick)
        }else{
            onEvent(QuranPageEvents.OnBackPressed)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(state.backgroundColor.asComposeColor())
    ) {
        if (state.quranPages.isNotEmpty()) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                HorizontalPager(

                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { index ->
                        QuranPage(
                            item = state.quranPages[index],
                            scale = state.pageScale,
                            useWhiteColor = state.useWhiteColor,
                            onTap = {
                                onEvent(QuranPageEvents.OnPageClick)
                            }
                        )
                }
            }
        }

        AnimatedVisibility(
            visible = state.showOptionsPanel,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                animationSpec = tween(250),
                initialOffsetY = { it }
            ) + fadeIn(tween(250)),
            exit = slideOutVertically(
                animationSpec = tween(250),
                targetOffsetY = { it }
            ) + fadeOut(tween(250))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    visible = state.showColorsPanel,
                    enter = slideInVertically(
                        animationSpec = tween(250),
                        initialOffsetY = { it }
                    ) + fadeIn(tween(250)),
                    exit = slideOutVertically(
                        animationSpec = tween(250),
                        targetOffsetY = { it }
                    ) + fadeOut(tween(250))
                ) {
                    ColorPickerPanel(
                        onColorPicked = {
                            onEvent(QuranPageEvents.OnColorPicked(it))
                        }
                    )
                }

                OptionsButtonsBar(
                    isBookmarked = state.isBookmarked,
                    isColorPanelVisible = state.showColorsPanel,
                    onColorClick = {
                        onEvent(QuranPageEvents.OnColorClick)
                    },
                    onSurasClick = { onEvent(QuranPageEvents.OnSurasClick) },
                    onBookmarkClick = {
                        onEvent(QuranPageEvents.OnBookmarkClicked)
                    },
                    onBookmarkLongClick = {
                        onEvent(QuranPageEvents.OnBookmarkLongClick)
                    },
                    onBookmarkedPagesClick = { onEvent(QuranPageEvents.OnBookmarksClick) },
                    onSearchClick = { onEvent(QuranPageEvents.OnSearchClick) },
                    onShareClick = { onEvent(QuranPageEvents.OnShareClick) },
                    onSettingsClick = { onEvent(QuranPageEvents.OnSettingsClick) },
                    onZoomClick = {
                        onEvent(QuranPageEvents.OnZoomClicked)
                    }
                )
            }
        }
    }

    val shareBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (state.shareTypeDialogState.isVisible) {
        ModalBottomSheet(
            sheetState = shareBottomSheetState,
            onDismissRequest = {
                onEvent(QuranPageEvents.OnShareDialogAction(DialogActions.OnDismiss))
            }
        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.choose_share_type),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.size(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(
                                QuranPageEvents.OnShareDialogAction(
                                    DialogActions.OnConfirm(
                                        ShareType.TEXT
                                    )
                                )
                            )
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(Res.string.text), color = MaterialTheme.colorScheme.onSurface)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onEvent(
                                QuranPageEvents.OnShareDialogAction(
                                    DialogActions.OnConfirm(
                                        ShareType.IMAGE
                                    )
                                )
                            )
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(text = stringResource(Res.string.image), color = MaterialTheme.colorScheme.onSurface)
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            TextButton(
                onClick = { onEvent(QuranPageEvents.OnShareDialogAction(DialogActions.OnDismiss)) },
                colors = textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(stringResource(Res.string.cancel))
            }
        }
    }
    }

    val bookmarkBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (state.bookmarkDialogState.isVisible) {
        ModalBottomSheet(
            sheetState = bookmarkBottomSheetState,
            onDismissRequest = {
                onEvent(QuranPageEvents.OnBookmarkDialogAction(DialogActionsWithQuery.OnDismiss))
            }
        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.add_note),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.size(8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.bookmarkDialogState.text,
                onValueChange = {
                    onEvent(
                        QuranPageEvents.OnBookmarkDialogAction(
                            DialogActionsWithQuery.OnQueryChange(
                                it
                            )
                        )
                    )
                },
                placeholder = { Text(stringResource(Res.string.note), color = MaterialTheme.colorScheme.onSurfaceVariant) },

            )
            Spacer(modifier = Modifier.size(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(
                    onClick = {
                        onEvent(
                            QuranPageEvents.OnBookmarkDialogAction(
                                DialogActionsWithQuery.OnConfirm(null)
                            )
                        )
                    },
                    colors = textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) { Text(stringResource(Res.string.save)) }
                TextButton(
                    onClick = {
                        onEvent(
                            QuranPageEvents.OnBookmarkDialogAction(
                                DialogActionsWithQuery.OnDismiss
                            )
                        )
                    },
                    colors = textButtonColors(contentColor = MaterialTheme.colorScheme.secondary)
                ) { Text(stringResource(Res.string.cancel)) }
            }
        }
    }
    }

    if (state.showZoomSheet) {
        ModalBottomSheet(
            sheetState = zoomBottomSheetState,
            onDismissRequest = { onEvent(QuranPageEvents.OnZoomDone) },
        ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                value = state.pageScaleSliderValue,
                onValueChange = {
                    onEvent(QuranPageEvents.OnPageSliderChange(it.toInt()))
                },
                steps = 8,
                valueRange = 1f..10f
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(10) { index ->
                    Text(
                        text = ((index + 1) * 10).toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
    }
}

@Composable
private fun QuranPage(
    item: QuranPageItem,
    scale: Float,
    useWhiteColor: Boolean,
    onTap: () -> Unit
) {
    val imageBitmap by rememberFileImageBitmap(item.imageFilePath)
    val textColor = if (useWhiteColor) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.scrim
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onTap)
    ) {
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale),
                colorFilter = if (useWhiteColor) {
                    ColorFilter.colorMatrix(ColorMatrix(WhiteColorFilter.matrix))
                } else null
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.surah, item.surahName),
                fontFamily = FontFamily(Font(resource = Res.font.naskh)),
                color = textColor,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            item.juzoaNumberText?.let {
                Text(
                    text = stringResource(Res.string.aljuzoa, it),
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily(Font(resource = Res.font.naskh))
                )
            }
        }
        Text(
            modifier = Modifier.align(Alignment.BottomCenter),
            text = item.pageNumberLocalized,
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily(Font(resource = Res.font.naskh))
        )
    }
}

@Composable
private fun ColorPickerPanel(
    onColorPicked: (ColorItem) -> Unit
) {
    Surface(
        tonalElevation = 4.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ColorDot(ColorItem.DKBLUE, Color(0xFF0C2942), onColorPicked)
            ColorDot(ColorItem.DKGRAY, Color(0xFF8F8F8F), onColorPicked)
            ColorDot(ColorItem.BEIGE, Color(0xFFF5F5DC), onColorPicked)
            ColorDot(ColorItem.WHITE, Color.White, onColorPicked)
        }
    }
}

@Composable
private fun ColorDot(
    item: ColorItem,
    color: Color,
    onColorPicked: (ColorItem) -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color, CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable { onColorPicked(item) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OptionsButtonsBar(
    isBookmarked: Boolean,
    isColorPanelVisible: Boolean,
    onColorClick: () -> Unit,
    onSurasClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onBookmarkLongClick: () -> Unit,
    onBookmarkedPagesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onShareClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onZoomClick: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            SearchCard(
                enabled = false,
                "",
                stringResource(Res.string.search),
                {},
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp).clickable {
                    onSearchClick()
                })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIcon(
                    icon = Icons.Default.Settings,
                    onClick = onSettingsClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ActionIcon(
                    icon = Icons.Default.Share,
                    onClick = onShareClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ActionIcon(
                    icon = Icons.Default.CollectionsBookmark,
                    onClick = onBookmarkedPagesClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ActionIcon(
                    icon = Icons.Default.ColorLens,
                    tint = if (isColorPanelVisible) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface,
                    onClick = onColorClick
                )
                ActionIcon(
                    icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    onClick = onBookmarkClick,
                    onLongClick = onBookmarkLongClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ActionIcon(
                    icon = Icons.Default.ZoomIn,
                    onClick = onZoomClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                ActionIcon(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    onClick = onSurasClick,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ActionIcon(
    icon: ImageVector,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    tint: Color = Color.Unspecified
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint)
    }
}

@Composable
private fun rememberFileImageBitmap(path: String): androidx.compose.runtime.State<ImageBitmap?> {
    return produceState<ImageBitmap?>(initialValue = null, key1 = path) {
        value = try {
            val bytes = PlatformFile(path).readBytes()
            bytes.decodeToImageBitmap()
        } catch (_: Exception) {
            null
        }
    }
}
