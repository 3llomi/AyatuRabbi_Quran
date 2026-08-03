package com.devlomi.shared.ui.suras

sealed class DialogActions{
    data class OnQueryChange(val query: String): DialogActions()
    data object OnDismiss: DialogActions()
    data object OnConfirm: DialogActions()
}