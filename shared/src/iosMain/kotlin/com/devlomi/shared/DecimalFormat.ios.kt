package com.devlomi.shared

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterNoStyle
import platform.Foundation.NSNumberFormatterStyle

actual object DecimalFormat {
    actual fun format(value: Int, locale: String): String {
        TODO("Not yet implemented")
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterNoStyle // Integer format
            this.locale = NSLocale(localeIdentifier = locale)
        }
        return formatter.stringFromNumber(NSNumber(value)) ?: value.toString()

    }
}