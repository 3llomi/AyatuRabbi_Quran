package com.devlomi.shared.domain

import com.devlomi.shared.platform

class ProperSizeCalc {
    fun getProperWidth(deviceWidth:Int): Int {
        //we use only 1260 on IOS
        if (platform() == "iOS") {
            return 1260
        }
        return  when {
            deviceWidth <= 320 -> 320
            deviceWidth <= 480 -> 480
            deviceWidth <= 800 -> 800
            deviceWidth <= 1280 -> 1024
            else -> 1260
        }
    }
}