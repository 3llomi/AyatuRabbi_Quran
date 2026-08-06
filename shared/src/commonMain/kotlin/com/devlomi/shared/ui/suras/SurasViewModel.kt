package com.devlomi.shared.ui.suras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.surah_names
import co.touchlab.kermit.Logger
import com.devlomi.shared.domain.model.Surah
import com.devlomi.shared.data.quran_datasource.QuranPageDataSource
import com.devlomi.shared.common.isDigitsOnly
import com.devlomi.shared.data.db.ayahinfo.AyahInfoRepository
import com.devlomi.shared.ui.quran_page.QuranPageNavigationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getStringArray

class SurasViewModel(
    private val quranPageDataSource: QuranPageDataSource,
    private val ayahInfoRepository: AyahInfoRepository,
) : ViewModel() {

    private lateinit var suras: List<Surah>
    private val _state = MutableStateFlow(SurasState())
    val state: StateFlow<SurasState> get() = _state.asStateFlow()

    private val navigationChannel = Channel<SurasNavigationEvent>()
    val navigationEvent: Flow<SurasNavigationEvent> = navigationChannel.receiveAsFlow()


    init {
        viewModelScope.launch {
            val surahNames = getStringArray(Res.array.surah_names)
            suras = surahNames.mapIndexed { index, surahName ->
                Surah(surahName, index + 1)
            }
            _state.update {
                it.copy(suras = suras)
            }
        }

    }


    fun onEvent(event: SurasEvents) {
        when (event) {
            is SurasEvents.OnQueryChange -> searchForSura(event.query)
            is SurasEvents.OnSurahClick -> {
                viewModelScope.launch {
                    val foundPageNumber =
                        ayahInfoRepository.getPageNumberBySurahNumber(event.surah.surahNumber)
                    navigationChannel.send(
                        SurasNavigationEvent.ToQuranPageWithPageNumber(
                            foundPageNumber
                        )
                    )
                }
            }

            is SurasEvents.JuzoaNumberDialogEvents -> {
                when (event.action) {
                    is DialogActionsWithQuery.OnQueryChange -> {

                        //verify page number is valid
                        _state.update {
                            it.copy(juzoaNumberDialogState = it.juzoaNumberDialogState.copy(text = event.action.query))
                        }
                    }

                    is DialogActionsWithQuery.OnDismiss -> {
                        _state.update {
                            it.copy(
                                juzoaNumberDialogState = it.juzoaNumberDialogState.copy(
                                    isVisible = false,
                                    showError = false
                                )
                            )
                        }
                    }

                    is DialogActionsWithQuery.OnConfirm<*> -> {
                        val pageNumber =
                            getPageNumberByJuzoaIfValid(_state.value.juzoaNumberDialogState.text)
                        if (pageNumber != null) {
                            //navigate to page number
                            _state.update {
                                it.copy(
                                    juzoaNumberDialogState = it.juzoaNumberDialogState.copy(
                                        isVisible = false,
                                        showError = false
                                    )
                                )
                            }
                            viewModelScope.launch {
                                navigationChannel.send(
                                    SurasNavigationEvent.ToQuranPageWithPageNumber(
                                        pageNumber
                                    )
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    juzoaNumberDialogState = it.juzoaNumberDialogState.copy(
                                        showError = true
                                    )
                                )
                            }
                        }
                    }
                }
            }

            SurasEvents.OnGoToJuzoaClick -> _state.update {
                it.copy(juzoaNumberDialogState = it.juzoaNumberDialogState.copy(isVisible = true))
            }

            SurasEvents.OnGoToPageClick -> _state.update {
                it.copy(pageNumberDialogState = it.pageNumberDialogState.copy(isVisible = true))
            }

            is SurasEvents.PageNumberDialogEvents -> {
                when (event.action) {
                    is DialogActionsWithQuery.OnQueryChange -> {
                        //verify page number is valid
                        _state.update {
                            it.copy(pageNumberDialogState = it.pageNumberDialogState.copy(text = event.action.query))
                        }
                    }

                    is DialogActionsWithQuery.OnDismiss -> {
                        _state.update {
                            it.copy(
                                pageNumberDialogState = it.pageNumberDialogState.copy(
                                    isVisible = false,
                                    showError = false
                                )
                            )
                        }
                    }

                    is DialogActionsWithQuery.OnConfirm<*> -> {
                        if (isPageNumberValid(_state.value.pageNumberDialogState.text)) {
                            //navigate to page number
                            _state.update {
                                it.copy(
                                    pageNumberDialogState = it.pageNumberDialogState.copy(
                                        isVisible = false,
                                        showError = false
                                    )
                                )
                            }
                            viewModelScope.launch {
                                navigationChannel.send(
                                    SurasNavigationEvent.ToQuranPageWithPageNumber(
                                        state.value.pageNumberDialogState.text.toInt().also {
                                            Logger.d{"Navigating to page number: $it - text ${state.value.pageNumberDialogState.text}"}
                                        }
                                    )
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    pageNumberDialogState = it.pageNumberDialogState.copy(
                                        showError = true
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun searchForSura(query: String) {
        _state.update { it.copy(query = query) }
        if (query.trim().isEmpty()) {
            _state.update { it.copy(suras = suras) }
        } else {
            _state.update {
                it.copy(suras = suras.filter { it.surahName.contains(query) }
                    .sortedBy { it.surahNumber })
            }

        }
    }

    private fun isPageNumberValid(page: String): Boolean {
        if (page.trim().isEmpty())
            return false

        if (page.isDigitsOnly()) {
            val allowedNumbers = (1..604)
            val pageNumber = page.toInt()
            if (allowedNumbers.contains(pageNumber)) {
                return true
            }
        }

        return false
    }

    private fun getPageNumberByJuzoaIfValid(juzoa: String): Int? {
        if (juzoa.isDigitsOnly()) {
            val allowedNumbers = (1..30)
            val juzoaNumber = juzoa.toInt()
            if (allowedNumbers.contains(juzoaNumber)) {
                try {
                    //return page number if valid
                    return quranPageDataSource.getPageForJuzArray()[juzoaNumber - 1]
                } catch (e: Exception) {
                }
            }
        }
        return null

    }


}