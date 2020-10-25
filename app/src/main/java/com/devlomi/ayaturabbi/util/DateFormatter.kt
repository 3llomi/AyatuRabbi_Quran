package com.devlomi.ayaturabbi.util

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {
    fun formatDate(timestamp: Long): String {

        val sdf = SimpleDateFormat("dd/MM/yyyy",Locale.US)
        val date = Date()
        date.time = timestamp
        return sdf.format(timestamp)
    }
}