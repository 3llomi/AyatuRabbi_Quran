package com.devlomi.shared.common

import android.os.Build
import java.util.Locale

actual object DecimalFormat {
    actual fun format(value: Int, locale: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) Locale.of(locale)
            else Locale(locale)
        return android.icu.text.DecimalFormat.getIntegerInstance().format(value)
    }
}