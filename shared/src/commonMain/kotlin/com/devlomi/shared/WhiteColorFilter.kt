package com.devlomi.shared

object WhiteColorFilter {
    private const val nightModeTextBrightness = 255f

    val matrix = floatArrayOf(
        -1f,
        0f,
        0f,
        0f,
        nightModeTextBrightness,
        0f,
        -1f,
        0f,
        0f,
        nightModeTextBrightness,
        0f,
        0f,
        -1f,
        0f,
        nightModeTextBrightness,
        0f,
        0f,
        0f,
        1f,
        0f
    )
}