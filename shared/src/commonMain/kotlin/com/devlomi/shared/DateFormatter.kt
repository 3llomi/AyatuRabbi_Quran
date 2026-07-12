package com.devlomi.shared

import androidx.room.Ignore
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

object DateFormatter {

    fun formatDate(timestamp: Long): String {
        // 1. Convert milliseconds into an Instant
        val instant = Instant.fromEpochMilliseconds(timestamp)

        // 2. Convert Instant to local date using a TimeZone (e.g., UTC or system default)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val date = localDateTime.date

        // 3. Define the "dd/MM/yyyy" format structure
        val pattern = kotlinx.datetime.LocalDate.Format {
            dayOfMonth(Padding.ZERO)         // "dd" (forces 2 digits)
            char('/')
            monthNumber(Padding.ZERO) // "MM" (forces 2 digits)
            char('/')
            year()                    // "yyyy"
        }

        // 4. Return the formatted string
        return date.format(pattern)
    }

}