package com.devlomi.shared.ui

import androidx.compose.runtime.Composable

@Composable
actual fun SetWindowFlag(keepScreenOn: Boolean) {
    //No Op on iOS
}

@Composable
actual fun ObserveWindowFocusChange(onFocusChange: (Boolean) -> Unit) {
    //No Op on iOS
}