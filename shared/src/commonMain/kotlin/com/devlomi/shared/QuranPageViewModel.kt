package com.devlomi.shared

import com.devlomi.shared.db.ayahinfo.AyahInfoRepository
import com.devlomi.shared.db.bookmark.BookmarkRepository
import com.devlomi.shared.db.quran_ar.QuranRepository
import com.devlomi.shared.quran_datasource.QuranPageDataSource
import com.devlomi.shared.settings.SettingsRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.rickclephas.kmp.observableviewmodel.ViewModel
import com.rickclephas.kmp.observableviewmodel.launch
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.cacheDir
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class QuranPageViewModel(
    private val settingsRepository: SettingsRepository,
    private val quranPageDataSource: QuranPageDataSource,
    private val quranRepository: QuranRepository,
    private val ayahInfoRepository: AyahInfoRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _quranPages = MutableStateFlow<MutableList<QuranPageItem>>(mutableListOf())
    @NativeCoroutinesState
    val quranPages: StateFlow<MutableList<QuranPageItem>> get() = _quranPages.asStateFlow()

    private val _backgroundColor = MutableStateFlow<String>(PageColors.DKBLUE)
    @NativeCoroutinesState
    val backgroundColor: StateFlow<String> get() = _backgroundColor.asStateFlow()

    private val _useWhiteColor = MutableStateFlow<Boolean>(false)
    @NativeCoroutinesState
    val useWhiteColor: StateFlow<Boolean> get() = _useWhiteColor.asStateFlow()

    private val _currentIndex = MutableStateFlow<Int>(settingsRepository.getCurrentIndex())
    @NativeCoroutinesState
    val currentIndex: StateFlow<Int> get() = _currentIndex.asStateFlow()

    private val _isBookmarked = MutableStateFlow<Boolean>(false)
    @NativeCoroutinesState
    val isBookmarked: StateFlow<Boolean> get() = _isBookmarked.asStateFlow()

    private val _shareText = MutableStateFlow<String?>(null)
    @NativeCoroutinesState
    val shareText: StateFlow<String?> get() = _shareText.asStateFlow()

    private val _shareImage = MutableStateFlow<String?>(null)
    @NativeCoroutinesState
    val shareImage: StateFlow<String?> get() = _shareImage.asStateFlow()

    private var backgroundColorItem =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())

    private val _pageScale = MutableStateFlow<Float>(0f)
    val pageScale: StateFlow<Float> get() = _pageScale.asStateFlow()


    private val _showZoomSheet = MutableStateFlow<Int?>(null)
    val showZoomSheet: StateFlow<Int?> get() = _showZoomSheet.asStateFlow()

    private var currentScale = settingsRepository.getScale()

    lateinit var quranPageItemsDataSource: List<QuranPageItem>
    fun loadData(surahNumber: Int?, pageNumber: Int?) {
        quranPageItemsDataSource = quranPageDataSource.getData()
        _quranPages.value.addAll(quranPageItemsDataSource)

        _quranPages.value = quranPageItemsDataSource.toMutableList()

        _pageScale.value = currentScale


        when {
            surahNumber != null -> {
                viewModelScope.launch (Dispatchers.IO) {
                    val foundPageNumber =
                        ayahInfoRepository.getPageNumberBySurahNumber(surahNumber)
                    withContext(Main) {
                        _currentIndex.value = foundPageNumber - 1
                        indexChanged()
                    }
                }

            }

            pageNumber != null -> {
                _currentIndex.value = pageNumber - 1
                indexChanged()

            }

            else -> {
                _currentIndex.value = _currentIndex.value
                indexChanged()

            }
        }


        updateBackgroundAndTextColors()

    }

    private fun indexChanged() {
        viewModelScope.launch(Dispatchers.IO) {

            currentIndex.value?.let { index ->
                try {

                    val isBookmarked = bookmarkRepository.bookmarkExists(index + 1)
                    withContext(Main) {
                        _isBookmarked.value = isBookmarked
                    }
                } catch (e: Exception) {

                }
            }

        }
    }

    fun onPageChanged(newIndex: Int) {
        _currentIndex.value = newIndex
        indexChanged()
    }

    fun colorPicked(colorItem: ColorItem) {
        backgroundColorItem = colorItem
        updateBackgroundAndTextColors()
    }

    fun bookmarkClicked() {
        isBookmarked.value?.let { isBookmarked ->

            _currentIndex.value?.let { index ->
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        if (isBookmarked) {
                            bookmarkRepository.unBookmark(index + 1)
                            withContext(Main) {
                                _isBookmarked.value = false
                            }
                        } else {
                            _quranPages.value?.getOrNull(index)?.let { quranPage ->
                                bookmarkRepository.bookmark(
                                    quranPage.pageNumber,
                                    quranPage.surahName,
                                    null
                                )
                                withContext(Main) {
                                    _isBookmarked.value = true
                                }
                            }
                        }
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }


    fun onStop() {
        settingsRepository.saveBackgroundColor(backgroundColorItem.name)
        _currentIndex.value?.let {
            settingsRepository.saveCurrentIndex(it)
        }
        settingsRepository.setScale(currentScale)
    }

    private fun updateBackgroundAndTextColors() {
        val backgroundColorRes = getBackgroundColorResource()
        _useWhiteColor.value = useWhiteTextColor()
        _backgroundColor.value = backgroundColorRes
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
    fun shareTypeChosen(shareType: ShareType) {
        val index = _currentIndex.value!!
        val pageNumber = index + 1
        quranPageDataSource.getSuraForPageArray().getOrNull(index)?.let { surahNumber ->


            when (shareType) {
                ShareType.TEXT -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            val shareText =
                                quranRepository.getShareTextForPage(pageNumber)
                            withContext(Main) {
                                _shareText.value = shareText
                            }
                        } catch (e: Exception) {

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
                    _shareImage.value = quranImageWithBacgkround
                }
            }

        }


    }

    fun bookmarkWithNote(note: String) {
        _currentIndex.value?.let { index ->
            _quranPages.value?.getOrNull(index)?.let { quranPage ->
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        bookmarkRepository.bookmark(
                            quranPage.pageNumber,
                            quranPage.surahName,
                            note
                        )
                        withContext(Main) {
                            _isBookmarked.value = true
                        }
                    } catch (e: Exception) {

                    }
                }
            }

        }


    }

    //prevent multiple calls when Fragment is re-created
    fun shareDone() {
        _shareText.value = null
        _shareImage.value = null
    }

    fun zoomDone() {
        _showZoomSheet.value = null
    }


    fun setPageScale(thumbPosition: Int) {
        val scale = ProgressMapper.mapToScale(thumbPosition)
        currentScale = scale
        _pageScale.value = scale
    }

    fun btnZoomClicked() {
        val mapToView = ProgressMapper.mapToView(currentScale)
        _showZoomSheet.value = mapToView
    }

}