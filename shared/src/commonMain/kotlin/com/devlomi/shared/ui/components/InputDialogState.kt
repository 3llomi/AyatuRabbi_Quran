package com.devlomi.shared.ui.components

data class InputDialogState(
    val text: String = "",
    val isVisible: Boolean = false,
    val showError: Boolean = false
)
data class DialogState(
    val isVisible: Boolean = false,
)