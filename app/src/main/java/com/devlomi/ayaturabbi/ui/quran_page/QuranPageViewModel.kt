package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.devlomi.ayaturabbi.view.ColorItem
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.datasource.quran_datasource.QuranPageDataSource
import com.devlomi.ayaturabbi.db.ayahinfo.AyahInfoRepository
import com.devlomi.ayaturabbi.db.bookmark.BookmarkRepository
import com.devlomi.ayaturabbi.db.quran_ar.QuranRepository
import com.devlomi.ayaturabbi.settings.SettingsRepository
import com.devlomi.ayaturabbi.util.ProgressMapper
import com.devlomi.ayaturabbi.util.ShareImageBackground
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class QuranPageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val quranPageDataSource: QuranPageDataSource,
    private val quranRepository: QuranRepository,
    private val ayahInfoRepository: AyahInfoRepository,
    private val bookmarkRepository: BookmarkRepository
) : ViewModel() {

    private val _quranPages = MutableLiveData<MutableList<QuranPageItem>>()
    val quranPages: LiveData<MutableList<QuranPageItem>> get() = _quranPages

    private val _backgroundColor = MutableLiveData<Int>()
    val backgroundColor: LiveData<Int> get() = _backgroundColor

    private val _useWhiteColor = MutableLiveData<Boolean>()
    val useWhiteColor: LiveData<Boolean> get() = _useWhiteColor

    private val _currentIndex = MutableLiveData<Int>(settingsRepository.getCurrentIndex())
    val currentIndex: LiveData<Int> get() = _currentIndex

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private val _shareText = MutableLiveData<String?>()
    val shareText: LiveData<String?> get() = _shareText

    private val _shareImage = MutableLiveData<String?>()
    val shareImage: LiveData<String?> get() = _shareImage

    private var backgroundColorItem =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())

    private val _pageScale = MutableLiveData<Float>()
    val pageScale: LiveData<Float> get() = _pageScale


    private val quranPageItemsDataSource =
        quranPageDataSource.getData()

    private val _showZoomSheet = MutableLiveData<Int?>()
    val showZoomSheet: LiveData<Int?> get() = _showZoomSheet

    private var currentScale = settingsRepository.getScale()

    fun loadData(surahNumber: Int?, pageNumber: Int?) {

        _quranPages.value?.addAll(quranPageItemsDataSource)

        _quranPages.value = quranPageItemsDataSource.toMutableList()

        _pageScale.value = currentScale


        when {
            surahNumber != null -> {
                viewModelScope.launch(IO) {
                    val foundPageNumber = ayahInfoRepository.getPageNumberBySurahNumber(surahNumber)
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
        viewModelScope.launch(IO) {

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
                viewModelScope.launch(IO) {
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

    private fun getBackgroundColorResource() = when (backgroundColorItem) {
        ColorItem.DKGRAY -> R.color.dkgray
        ColorItem.BEIGE -> R.color.beige
        ColorItem.WHITE -> R.color.white
        else -> R.color.dkblue
    }

    fun shareTypeChosen(shareType: ShareType) {
        val index = _currentIndex.value!!
        val pageNumber = index + 1
        quranPageDataSource.getSuraForPageArray().getOrNull(index)?.let { surahNumber ->


            when (shareType) {
                ShareType.TEXT -> {
                    viewModelScope.launch(IO) {
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
                    val quranImageFile = quranRepository.getQuranImageFile(pageNumber)

                    val finalFile = File(
                        File(context.cacheDir, "share_images"),
                        UUID.randomUUID().toString() + ".png"
                    )

                    val quranImageWithBacgkround =
                        ShareImageBackground.addBackgroundColorToImage(
                            quranImageFile,
                            ContextCompat.getColor(context, getBackgroundColorResource()),
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
                viewModelScope.launch(IO) {
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


