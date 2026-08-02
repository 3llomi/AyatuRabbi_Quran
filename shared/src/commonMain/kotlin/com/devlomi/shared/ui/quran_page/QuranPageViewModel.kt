package com.devlomi.shared.ui.quran_page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlomi.shared.domain.ColorItem
import com.devlomi.shared.domain.PageColors
import com.devlomi.shared.domain.ProgressMapper
import com.devlomi.shared.domain.model.QuranPageItem
import com.devlomi.shared.domain.ShareImageBackground
import com.devlomi.shared.domain.ShareType
import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import com.devlomi.shared.data.settings.SettingsRepository
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class QuranPageViewModel(
    savedStateHandle: SavedStateHandle,
    private val settingsRepository: SettingsRepository,
    private val quranPageDataSource: QuranPageDataSource,
    private val quranRepository: com.devlomi.shared.data.db.quran_ar.QuranRepository,
    private val ayahInfoRepository: com.devlomi.shared.data.db.ayahinfo.AyahInfoRepository,
    private val bookmarkRepository: com.devlomi.shared.data.db.bookmark.BookmarkRepository
) : ViewModel() {

    private var backgroundColorItem =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())

    private var currentScale = settingsRepository.getScale()

    private val _state = MutableStateFlow<QuranPageState>(
        QuranPageState(
            currentIndex = settingsRepository.getCurrentIndex(),
            pageScale = currentScale,
            backgroundColor = getBackgroundColorResource(),
            useWhiteColor = useWhiteTextColor()
        )
    )
    val state: StateFlow<QuranPageState> get() = _state.asStateFlow()

    fun onEvent(event: QuranPageEvents) {
        when (event) {
            is QuranPageEvents.OnPageChanged -> onPageChanged(event.index)
            is QuranPageEvents.OnColorPicked -> colorPicked(event.colorItem)
            QuranPageEvents.OnBookmarkClicked -> bookmarkClicked()
            QuranPageEvents.OnStop -> onStop()
            is QuranPageEvents.OnShareTypeChosen -> shareTypeChosen(event.shareType)
            is QuranPageEvents.OnBookmarkWithNote -> bookmarkWithNote(event.note)
            QuranPageEvents.OnShareDone -> shareDone()
            QuranPageEvents.OnZoomDone -> zoomDone()
            is QuranPageEvents.OnSetPageScale -> setPageScale(event.thumbPosition)
            QuranPageEvents.OnZoomClicked -> btnZoomClicked()
        }
    }

    var quranPageItemsDataSource: List<QuranPageItem>
    init {
        val surahNumber = savedStateHandle.get<Int?>("surahNumber")
        val pageNumber = savedStateHandle.get<Int?>("pageNumber")
        quranPageItemsDataSource = quranPageDataSource.getData()
        _state.update {
            it.copy(
                quranPages = quranPageItemsDataSource,
                pageScale = currentScale
            )
        }


        when {
            surahNumber != null -> {
                viewModelScope.launch(Dispatchers.Default) {
                    val foundPageNumber =
                        ayahInfoRepository.getPageNumberBySurahNumber(surahNumber)
                    withContext(Dispatchers.Main) {
                        _state.update { state ->
                            state.copy(currentIndex = foundPageNumber - 1)
                        }
                        indexChanged()
                    }
                }

            }

            pageNumber != null -> {
                _state.update { it.copy(currentIndex = pageNumber - 1) }
                indexChanged()

            }

            else -> {
                indexChanged()

            }
        }


        updateBackgroundAndTextColors()

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
                            _state.update { it.copy(shareText = shareText) }
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
                _state.update { it.copy(shareImage = quranImageWithBacgkround) }
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
    private fun shareDone() {
        _state.update { it.copy(shareText = null, shareImage = null) }
    }

    private fun zoomDone() {
        _state.update { it.copy(showZoomSheet = null) }
    }


    private fun setPageScale(thumbPosition: Int) {
        val scale = ProgressMapper.mapToScale(thumbPosition)
        currentScale = scale
        _state.update { it.copy(pageScale = scale) }
    }

    private fun btnZoomClicked() {
        val mapToView = ProgressMapper.mapToView(currentScale)
        _state.update { it.copy(showZoomSheet = mapToView) }
    }

}