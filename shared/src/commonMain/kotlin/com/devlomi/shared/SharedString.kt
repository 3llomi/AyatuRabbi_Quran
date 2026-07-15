package com.devlomi.shared

expect class SharedString{
    fun getString(string: Strings, vararg formatArgs: Any = emptyArray()): String
    fun getStringArray(array: StringArrays): List<String>
}