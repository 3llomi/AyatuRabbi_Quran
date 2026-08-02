package com.devlomi.shared.ui.suras

import com.devlomi.shared.domain.model.Surah

data class SurasState(
    val suras: List<Surah> = listOf(),
    val query: String = ""
)
