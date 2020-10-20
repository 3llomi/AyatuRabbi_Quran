package com.devlomi.ayaturabbi.extensions

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display

fun Activity.deviceWidthPixels():Int {
    var width = 0
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        val display: Display? = this.display
        display!!.getRealMetrics(displayMetrics)
        displayMetrics.widthPixels
    } else {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels
        width
    }

}

fun Activity.deviceHeightPixels():Int {
    var height = 0
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val displayMetrics = DisplayMetrics()
        val display: Display? = this.display
        display!!.getRealMetrics(displayMetrics)
        displayMetrics.heightPixels
    } else {
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        height
    }

}