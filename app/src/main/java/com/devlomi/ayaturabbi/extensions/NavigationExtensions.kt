package com.devlomi.ayaturabbi.extensions

import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.navigateSafely(@IdRes fragmentId:Int, @IdRes action:Int){
    if (this.currentDestination?.id != fragmentId) {
        this.navigate(action)
    }
}