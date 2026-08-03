package com.devlomi.shared.ui.suras

import com.devlomi.shared.domain.model.Surah

sealed class SurasEvents {
    data class OnSurahClick(val surah: Surah) : SurasEvents()
    data class OnQueryChange(val query: String) : SurasEvents()
    object OnGoToJuzoaClick : SurasEvents()
    object OnGoToPageClick : SurasEvents()
    data class PageNumberDialogEvents(val action: DialogActions): SurasEvents()
    data class JuzoaNumberDialogEvents(val action: DialogActions) : SurasEvents()
}

//data class DialogActions(
//    val onQueryChange: (String) -> Unit ={""},
//    val onDismiss: () -> Unit = {},
//    val onConfirm: () -> Unit = {},
//)

