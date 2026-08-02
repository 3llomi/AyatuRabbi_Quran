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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.domain.ShareType
import com.devlomi.shared.domain.WhiteColorFilter
import com.devlomi.shared.domain.model.QuranPageItem
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuranPageScreen(
	onOpenSuras: () -> Unit = {},
	onOpenBookmarks: () -> Unit = {},
	onOpenSearch: () -> Unit = {},
	onOpenSettings: () -> Unit = {},
	onShareText: (String) -> Unit = {},
	onShareImage: (String) -> Unit = {},
	viewModel: QuranPageViewModel = koinViewModel()
) {
	val state by viewModel.state.collectAsStateWithLifecycleCompat()

	var showOptionsPanel by rememberSaveable { mutableStateOf(false) }
	var showColorPanel by rememberSaveable { mutableStateOf(false) }
	var showShareDialog by rememberSaveable { mutableStateOf(false) }
	var showNoteDialog by rememberSaveable { mutableStateOf(false) }
	var bookmarkNote by rememberSaveable { mutableStateOf("") }

	val pagerState = rememberPagerState(
		initialPage = 0,
		pageCount = { state.quranPages.size }
	)

	LaunchedEffect(state.currentIndex, state.quranPages.size) {
		if (state.quranPages.isNotEmpty() && state.currentIndex != pagerState.currentPage) {
			pagerState.scrollToPage(state.currentIndex.coerceIn(0, state.quranPages.lastIndex))
		}
	}

	LaunchedEffect(pagerState.currentPage) {
		if (state.quranPages.isNotEmpty() && pagerState.currentPage != state.currentIndex) {
			viewModel.onEvent(QuranPageEvents.OnPageChanged(pagerState.currentPage))
		}
	}

	LaunchedEffect(state.shareText) {
		state.shareText?.let {
			onShareText(it)
			viewModel.onEvent(QuranPageEvents.OnShareDone)
		}
	}

	LaunchedEffect(state.shareImage) {
		state.shareImage?.let {
			onShareImage(it)
			viewModel.onEvent(QuranPageEvents.OnShareDone)
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(state.backgroundColor.asComposeColor())
	) {
		if (state.quranPages.isNotEmpty()) {
			HorizontalPager(
				state = pagerState,
				modifier = Modifier.fillMaxSize()
			) { index ->
				QuranPage(
					item = state.quranPages[index],
					scale = state.pageScale,
					useWhiteColor = state.useWhiteColor,
					onTap = { showOptionsPanel = !showOptionsPanel }
				)
			}
		}

		AnimatedVisibility(
			visible = showOptionsPanel,
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
					visible = showColorPanel,
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
							viewModel.onEvent(QuranPageEvents.OnColorPicked(it))
						}
					)
				}

				OptionsButtonsBar(
					isBookmarked = state.isBookmarked,
					isColorPanelVisible = showColorPanel,
					onColorClick = { showColorPanel = !showColorPanel },
					onSurasClick = onOpenSuras,
					onBookmarkClick = {
						viewModel.onEvent(QuranPageEvents.OnBookmarkClicked)
					},
					onBookmarkLongClick = {
						showNoteDialog = true
					},
					onBookmarkedPagesClick = onOpenBookmarks,
					onSearchClick = onOpenSearch,
					onShareClick = { showShareDialog = true },
					onSettingsClick = onOpenSettings,
					onZoomClick = {
						viewModel.onEvent(QuranPageEvents.OnZoomClicked)
					}
				)
			}
		}
	}

	if (showShareDialog) {
		AlertDialog(
			onDismissRequest = { showShareDialog = false },
			title = { Text("Share") },
			text = {
				Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
					Text(
						text = "Text",
						modifier = Modifier.clickable {
							showShareDialog = false
							viewModel.onEvent(QuranPageEvents.OnShareTypeChosen(ShareType.TEXT))
						}
					)
					Text(
						text = "Image",
						modifier = Modifier.clickable {
							showShareDialog = false
							viewModel.onEvent(QuranPageEvents.OnShareTypeChosen(ShareType.IMAGE))
						}
					)
				}
			},
			confirmButton = {
				TextButton(onClick = { showShareDialog = false }) { Text("Close") }
			}
		)
	}

	if (showNoteDialog) {
		AlertDialog(
			onDismissRequest = { showNoteDialog = false },
			title = { Text("Add note") },
			text = {
				TextField(
					value = bookmarkNote,
					onValueChange = { bookmarkNote = it },
					placeholder = { Text("Note") }
				)
			},
			confirmButton = {
				TextButton(onClick = {
					viewModel.onEvent(QuranPageEvents.OnBookmarkWithNote(bookmarkNote))
					bookmarkNote = ""
					showNoteDialog = false
				}) { Text("Save") }
			},
			dismissButton = {
				TextButton(onClick = { showNoteDialog = false }) { Text("Cancel") }
			}
		)
	}

	state.showZoomSheet?.let { progress ->
		var sliderValue by remember(progress) { mutableStateOf(progress.toFloat()) }
		AlertDialog(
			onDismissRequest = { viewModel.onEvent(QuranPageEvents.OnZoomDone) },
			title = { Text("Zoom") },
			text = {
				Slider(
					value = sliderValue,
					onValueChange = {
						sliderValue = it
						viewModel.onEvent(QuranPageEvents.OnSetPageScale(it.toInt()))
					},
					valueRange = 0f..100f
				)
			},
			confirmButton = {
				TextButton(onClick = { viewModel.onEvent(QuranPageEvents.OnZoomDone) }) {
					Text("Done")
				}
			}
		)
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
	val textColor = if (useWhiteColor) Color.White else Color.Black

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

		Column(
			modifier = Modifier
				.align(Alignment.TopEnd)
				.padding(16.dp),
			horizontalAlignment = Alignment.End
		) {
			item.juzoaNumberText?.let {
				Text(
					text = "الجزء $it",
					color = textColor,
					fontWeight = FontWeight.Bold
				)
			}
			Text(
				text = "سورة ${item.surahName}",
				color = textColor
			)
			Text(
				text = item.pageNumberLocalized,
				color = textColor
			)
		}
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
			.size(34.dp)
			.background(color, CircleShape)
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
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp, vertical = 10.dp),
			horizontalArrangement = Arrangement.SpaceEvenly,
			verticalAlignment = Alignment.CenterVertically
		) {
			ActionIcon(
				icon = Icons.Default.ColorLens,
				tint = if (isColorPanelVisible) MaterialTheme.colorScheme.secondary else Color.Unspecified,
				onClick = onColorClick
			)
			ActionIcon(icon = Icons.Default.MenuBook, onClick = onSurasClick)
			ActionIcon(
				icon = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
				onClick = onBookmarkClick,
				onLongClick = onBookmarkLongClick
			)
			ActionIcon(icon = Icons.Default.Bookmark, onClick = onBookmarkedPagesClick)
			ActionIcon(icon = Icons.Default.Search, onClick = onSearchClick)
			ActionIcon(icon = Icons.Default.Share, onClick = onShareClick)
			ActionIcon(icon = Icons.Default.Settings, onClick = onSettingsClick)
			ActionIcon(icon = Icons.Default.ZoomIn, onClick = onZoomClick)
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

@Composable
private fun StateFlow<QuranPageState>.collectAsStateWithLifecycleCompat(): androidx.compose.runtime.State<QuranPageState> {
	return this.collectAsState()
}

private fun String.asComposeColor(): Color {
	val raw = removePrefix("#")
	val argb = when (raw.length) {
		6 -> (0xFF000000 or raw.toLong(16))
		8 -> raw.toLong(16)
		else -> 0xFF0C2942
	}
	return Color(argb)
}