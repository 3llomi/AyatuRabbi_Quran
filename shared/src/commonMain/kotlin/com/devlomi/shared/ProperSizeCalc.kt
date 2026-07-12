package com.devlomi.shared

//TODO SHOULD WE DO A VARIANT FOR IOS?
class ProperSizeCalc {
    fun getProperWidth(deviceWidth:Int): Int {
        return  when {
            deviceWidth <= 320 -> 320
            deviceWidth <= 480 -> 480
            deviceWidth <= 800 -> 800
            deviceWidth <= 1280 -> 1024
            else -> 1260
        }
    }
}