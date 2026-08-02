package com.devlomi.shared.ui.suras

import com.devlomi.shared.domain.model.Surah

sealed class SurasEvents {
    data class OnSurahClick(val surah: Surah) : SurasEvents()
    data class OnQueryChange(val query: String) : SurasEvents()
}