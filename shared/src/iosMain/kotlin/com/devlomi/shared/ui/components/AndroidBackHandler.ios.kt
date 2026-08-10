package com.devlomi.shared.ui.components

import androidx.compose.runtime.Composable

@Composable
actual fun AndroidBackHandler(enabled: Boolean, onBack: () -> Unit) {
    //No Op on iOS
}