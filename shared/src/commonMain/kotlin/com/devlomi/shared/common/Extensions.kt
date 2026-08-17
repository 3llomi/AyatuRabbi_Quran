package com.devlomi.shared.common

import androidx.compose.ui.graphics.Color

fun String.isDigitsOnly() = all { it.isDigit() }
fun String.asComposeColor(): Color {
    val raw = removePrefix("#")
    val argb = when (raw.length) {
        6 -> (0xFF000000 or raw.toLong(16))
        8 -> raw.toLong(16)
        else -> 0xFF0C2942
    }
    return Color(argb)
}