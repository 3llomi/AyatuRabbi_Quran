package com.devlomi.shared.ui.suras

sealed interface DialogActions {
    data object OnDismiss : DialogActions
    data class OnConfirm<T>(val data: T? = null) : DialogActions
}

sealed interface DialogActionsWithQuery {
    data object OnDismiss : DialogActionsWithQuery
    data class OnConfirm<T>(val data: T? = null) : DialogActionsWithQuery
    data class OnQueryChange(val query: String) : DialogActionsWithQuery
}


