package com.devlomi.shared.ui

import androidx.compose.runtime.Composable
import platform.UIKit.UIApplication

@Composable
actual fun SetWindowFlag(keepScreenOn: Boolean) {
    UIApplication.sharedApplication.idleTimerDisabled = keepScreenOn
}

@Composable
actual fun ObserveWindowFocusChange(onFocusChange: (Boolean) -> Unit) {
    //No Op on iOS
}