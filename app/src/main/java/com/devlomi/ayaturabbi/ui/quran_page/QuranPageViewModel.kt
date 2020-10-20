package com.devlomi.ayaturabbi.ui.quran_page

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.devlomi.ayaturabbi.ColorItem
import com.devlomi.ayaturabbi.R
import com.devlomi.ayaturabbi.constants.Constants
import com.devlomi.ayaturabbi.db.bookmark.BookmarkRepository
import com.devlomi.ayaturabbi.db.quran_ar.QuranRepository
import com.devlomi.ayaturabbi.settings.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

class QuranPageViewModel @ViewModelInject constructor(
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

    private val _shareText = MutableLiveData<String>()
    val shareText: LiveData<String> get() = _shareText

    private val _shareImage = MutableLiveData<String>()
    val shareImage: LiveData<String> get() = _shareImage

    private var backgroundColorItem =
        ColorItem.fromName(settingsRepository.getBackgroundColorName())


    fun loadData(surahNumber: Int?, pageNumber: Int?) {
        val quranPageItemsDataSource =
            quranPageDataSource.getData()

        _quranPages.value?.addAll(quranPageItemsDataSource)

//        quranPageItems.addAll(quranPageItemsDataSource)
        _quranPages.value = quranPageItemsDataSource.toMutableList()


        if (surahNumber == null || surahNumber == Constants.DEFAULT_INDEX) {
            if (pageNumber != null) {
                _currentIndex.value = pageNumber - 1
            } else {
                _currentIndex.value = _currentIndex.value
                indexChanged()
            }
        } else {
            viewModelScope.launch(IO) {
                val foundPageNumber = ayahInfoRepository.getPageNumberBySurahNumber(surahNumber)
                withContext(Main) {
                    _currentIndex.value = foundPageNumber - 1
                    indexChanged()
                }
            }
        }




        updateBackgroundAndTextColors()
    }

    private fun indexChanged() {
        viewModelScope.launch(IO) {

            currentIndex.value?.let { index ->
                val isBookmarked = bookmarkRepository.bookmarkExists(index + 1)
                withContext(Main) {
                    _isBookmarked.value = isBookmarked
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

                }


            }
        }
    }


    fun onStop() {
        settingsRepository.saveBackgroundColor(backgroundColorItem.name)
        _currentIndex.value?.let {
            settingsRepository.saveCurrentIndex(it)
        }
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
        currentIndex.value?.let { index ->
            val pageNumber = index + 1

            when (shareType) {
                ShareType.TEXT -> {
                    viewModelScope.launch(IO) {
                        val shareText = quranRepository.getShareTextForPage(pageNumber)
                        withContext(Main) {
                            _shareText.value = shareText
                        }
                    }
                }

                ShareType.IMAGE -> {
                    val quranImageFile = quranRepository.getQuranImageFile(pageNumber)

                    val finalFile = File(
                        File(context.cacheDir, "share_images"),
                        UUID.randomUUID().toString() + ".png"
                    )

                    val quranImageWithBacgkround = ShareImageBackground.addBackgroundColorToImage(
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

                    bookmarkRepository.bookmark(
                        quranPage.pageNumber,
                        quranPage.surahName,
                        note
                    )
                    withContext(Main) {
                        _isBookmarked.value = true
                    }
                }
            }

        }

    }

    override fun onCleared() {
        super.onCleared()
        Log.d("3llomi","onCleared ")
    }
}


