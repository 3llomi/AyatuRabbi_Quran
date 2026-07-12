package com.devlomi.shared


expect object DecimalFormat{
    fun format(value:Int,locale: String): String
}