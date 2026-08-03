package com.devlomi.shared.ui.suras

import com.devlomi.shared.domain.model.Surah

data class SurasState(
    val suras: List<Surah> = listOf(),
    val query: String = "",
    val pageNumberDialogState: InputDialogState = InputDialogState(),
    val juzoaNumberDialogState: InputDialogState = InputDialogState()
)

data class InputDialogState(
    val text: String = "",
    val isVisible: Boolean = false,
    val showError: Boolean = false
)
