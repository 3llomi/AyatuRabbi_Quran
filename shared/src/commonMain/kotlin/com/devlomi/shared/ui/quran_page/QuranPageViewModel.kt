package com.devlomi.shared.ui.quran_page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.domain.PageColors
import com.devlomi.shared.domain.ProgressMapper
import com.devlomi.shared.domain.model.QuranPageItem
import com.devlomi.shared.domain.ShareImageBackground
import com.devlomi.shared.domain.ShareType
import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import com.devlomi.shared.data.settings.SettingsRepository
import com.devlomi.shared.ui.Screen
import com.devlomi.shared.ui.suras.DialogActions
import com.devlomi.shared.ui.suras.DialogActionsWithQuery
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class QuranPageViewModel(
    private val settingsRepository: SettingsRepository,
    private val quranPageDataSource: QuranPageDataSource,
    private val quranRepository: com.devlomi.shared.data.db.quran_ar.QuranRepository,
    private val bookmarkRepository: com.devlomi.shared.data.db.bookmark.BookmarkRepository
) : ViewModel() {

    private val navigationChannel = Channel<QuranPageNavigationEvent>()
    val navigationEvent: Flow<QuranPageNavigationEvent> = navigationChannel.receiveAsFlow()

    private var backgroundColorItem =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())

    private var currentScale = settingsRepository.getScale()

    private val _state = MutableStateFlow(
        QuranPageState(
            currentIndex = settingsRepository.getCurrentIndex(),
            pageScale = currentScale,
            pageScaleSliderValue = ProgressMapper.mapToView(currentScale).toFloat(),
            backgroundColor = getBackgroundColorResource(),
            useWhiteColor = useWhiteTextColor()
        )
    )
    val state: StateFlow<QuranPageState> get() = _state.asStateFlow()

    fun onEvent(event: QuranPageEvents) {
        when (event) {
            is QuranPageEvents.OnPageChanged -> {
                _state.update { it.copy(showOptionsPanel = false) }
                onPageChanged(event.index)
            }
            is QuranPageEvents.OnPageSwipe -> onPageChanged(event.index)
            is QuranPageEvents.OnColorPicked -> colorPicked(event.colorItem)
            QuranPageEvents.OnBookmarkClicked -> bookmarkClicked()
            QuranPageEvents.OnStop -> onStop()
            QuranPageEvents.OnZoomDone -> zoomDone()
            is QuranPageEvents.OnPageSliderChange -> setPageScale(event.thumbPosition)
            QuranPageEvents.OnZoomClicked -> btnZoomClicked()
            QuranPageEvents.OnSurasClick -> navigateTo(QuranPageNavigationEvent.ToSuras)
            QuranPageEvents.OnBookmarksClick -> navigateTo(QuranPageNavigationEvent.ToBookmarks)
            QuranPageEvents.OnSearchClick -> navigateTo(QuranPageNavigationEvent.ToSearch)
            QuranPageEvents.OnSettingsClick -> navigateTo(QuranPageNavigationEvent.ToSettings)
            QuranPageEvents.OnShareClick -> showShareDialog()
            QuranPageEvents.OnPageClick -> onPageClick()
            QuranPageEvents.OnColorClick -> toggleColorPanel()
            QuranPageEvents.OnBookmarkLongClick -> showBookmarkDialog()
            is QuranPageEvents.OnShareDialogAction -> handleShareDialogAction(event.action)
            is QuranPageEvents.OnBookmarkDialogAction -> handleBookmarkDialogAction(event.action)
            is QuranPageEvents.OnBackPressed -> viewModelScope.launch {
                navigationChannel.send(QuranPageNavigationEvent.BackPressed)
            }
        }
    }

    private fun navigateTo(navEvent: QuranPageNavigationEvent) {
        viewModelScope.launch {
            navigationChannel.send(navEvent)
        }
    }

    private fun showShareDialog() {
        _state.update { it.copy(shareTypeDialogState = it.shareTypeDialogState.copy(isVisible = true)) }
    }

    private fun showBookmarkDialog() {
        _state.update { it.copy(bookmarkDialogState = it.bookmarkDialogState.copy(isVisible = true)) }
    }

    private fun onPageClick() {
        if (state.value.showZoomSheet) {
            _state.update { it.copy(showZoomSheet = false) }
            return
        }
        _state.update { it.copy(showOptionsPanel = !it.showOptionsPanel) }
    }

    private fun toggleColorPanel() {
        _state.update { it.copy(showColorsPanel = !it.showColorsPanel) }
    }

    private fun handleShareDialogAction(action: DialogActions) {
        when (action) {
            is DialogActions.OnConfirm<*> -> {

                shareTypeChosen(action.data as ShareType)
                _state.update {
                    it.copy(
                        shareTypeDialogState = it.shareTypeDialogState.copy(
                            isVisible = false,
                        )
                    )
                }
            }

            DialogActions.OnDismiss -> {
                _state.update {
                    it.copy(
                        shareTypeDialogState = it.shareTypeDialogState.copy(
                            isVisible = false,
                        )
                    )
                }
            }

        }
    }

    private fun handleBookmarkDialogAction(action: DialogActionsWithQuery) {
        when (action) {
            is DialogActionsWithQuery.OnConfirm<*> -> {
                bookmarkWithNote(state.value.bookmarkDialogState.text)
                _state.update {
                    it.copy(
                        bookmarkDialogState = it.bookmarkDialogState.copy(
                            isVisible = false,
                            text = ""
                        )
                    )
                }
            }

            DialogActionsWithQuery.OnDismiss -> {
                _state.update {
                    it.copy(
                        bookmarkDialogState = it.bookmarkDialogState.copy(
                            isVisible = false,
                            text = ""
                        )
                    )
                }
            }

            is DialogActionsWithQuery.OnQueryChange -> {
                _state.update { it.copy(bookmarkDialogState = it.bookmarkDialogState.copy(text = action.query)) }
            }


        }
    }

    var quranPageItemsDataSource: List<QuranPageItem> = quranPageDataSource.getData()

    init {
        _state.update {
            it.copy(
                quranPages = quranPageItemsDataSource,
                pageScale = currentScale
            )
        }


        updateBackgroundAndTextColors()
        indexChanged()

    }

    private fun indexChanged() {
        viewModelScope.launch(Dispatchers.Default) {
            val index = state.value.currentIndex
            try {
                val isBookmarked = bookmarkRepository.bookmarkExists(index + 1)
                withContext(Dispatchers.Main) {
                    _state.update { it.copy(isBookmarked = isBookmarked) }
                }
            } catch (_: Exception) {
            }

        }
    }

    private fun onPageChanged(newIndex: Int) {
        _state.update { it.copy(currentIndex = newIndex) }
        indexChanged()
    }

    private fun colorPicked(colorItem: ColorItem) {
        backgroundColorItem = colorItem
        updateBackgroundAndTextColors()
    }

    private fun bookmarkClicked() {
        val isBookmarked = state.value.isBookmarked
        val index = state.value.currentIndex

        viewModelScope.launch(Dispatchers.Default) {
            try {
                if (isBookmarked) {
                    bookmarkRepository.unBookmark(index + 1)
                    withContext(Dispatchers.Main) {
                        _state.update { it.copy(isBookmarked = false) }
                    }
                } else {
                    state.value.quranPages.getOrNull(index)?.let { quranPage ->
                        bookmarkRepository.bookmark(
                            quranPage.pageNumber,
                            quranPage.surahName,
                            null
                        )
                        withContext(Dispatchers.Main) {
                            _state.update { it.copy(isBookmarked = true) }
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }
    }


    private fun onStop() {
        settingsRepository.saveBackgroundColor(backgroundColorItem.name)
        settingsRepository.saveCurrentIndex(state.value.currentIndex)
        settingsRepository.setScale(currentScale)
    }

    private fun updateBackgroundAndTextColors() {
        val backgroundColorRes = getBackgroundColorResource()
        _state.update {
            it.copy(
                useWhiteColor = useWhiteTextColor(),
                backgroundColor = backgroundColorRes
            )
        }
    }

    private fun useWhiteTextColor() =
        backgroundColorItem == ColorItem.DKBLUE || backgroundColorItem == ColorItem.DKGRAY

    private fun getBackgroundColorResource(): String = when (backgroundColorItem) {
        ColorItem.DKGRAY -> PageColors.DKGRAY
        ColorItem.BEIGE -> PageColors.BEIGE
        ColorItem.WHITE -> PageColors.WHITE
        else -> PageColors.DKBLUE
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun shareTypeChosen(shareType: ShareType) {
        val index = state.value.currentIndex
        val pageNumber = index + 1
        if (quranPageDataSource.getSuraForPageArray().getOrNull(index) == null) return


        when (shareType) {
            ShareType.TEXT -> {
                viewModelScope.launch(Dispatchers.Default) {
                    try {
                        val shareText =
                            quranRepository.getShareTextForPage(pageNumber)
                        withContext(Dispatchers.Main) {
                            navigateTo(QuranPageNavigationEvent.ShareText(shareText))
                        }
                    } catch (_: Exception) {
                    }
                }

            }

            ShareType.IMAGE -> {
                val quranImageFile = PlatformFile(quranRepository.getQuranImageFile(pageNumber))

                val finalFile = PlatformFile(
                    PlatformFile(FileKit.cacheDir, "share_images"),
                    Uuid.random().toString() + ".png"
                )

                val quranImageWithBacgkround =
                    ShareImageBackground.addBackgroundColorToImage(
                        quranImageFile,
                        getBackgroundColorResource(),
                        useWhiteTextColor(),
                        finalFile
                    )
                navigateTo(QuranPageNavigationEvent.ShareImage(quranImageWithBacgkround))
            }
        }


    }

    private fun bookmarkWithNote(note: String) {
        val index = state.value.currentIndex
        state.value.quranPages.getOrNull(index)?.let { quranPage ->
            viewModelScope.launch(Dispatchers.Default) {
                try {
                    bookmarkRepository.bookmark(
                        quranPage.pageNumber,
                        quranPage.surahName,
                        note
                    )
                    withContext(Dispatchers.Main) {
                        _state.update { it.copy(isBookmarked = true) }
                    }
                } catch (_: Exception) {

                }
            }
        }


    }

    //prevent multiple calls when Fragment is re-created


    private fun zoomDone() {
        _state.update { it.copy(showZoomSheet = false) }
    }


    private fun setPageScale(thumbPosition: Int) {
        val scale = ProgressMapper.mapToScale(thumbPosition)
        Logger.d { "ThumbPosition $thumbPosition - scale: $scale" }
        currentScale = scale
        _state.update { it.copy(pageScale = scale, pageScaleSliderValue = thumbPosition.toFloat()) }
    }

    private fun btnZoomClicked() {
        _state.update { it.copy(showZoomSheet = true) }
    }

}