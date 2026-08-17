package com.devlomi.shared.common

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterNoStyle

actual object DecimalFormat {
    actual fun format(value: Int, locale: String): String {
        val formatter = NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterNoStyle // Integer format
            this.locale = NSLocale(localeIdentifier = locale)
        }
        return formatter.stringFromNumber(NSNumber(value)) ?: value.toString()

    }
}