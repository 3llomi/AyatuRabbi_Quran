package com.devlomi.shared.common


expect object DecimalFormat{
    fun format(value:Int,locale: String): String
}