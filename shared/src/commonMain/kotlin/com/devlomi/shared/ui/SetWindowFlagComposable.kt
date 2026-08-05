package com.devlomi.shared.ui

import androidx.compose.runtime.Composable

@Composable
expect fun SetWindowFlag(keepScreenOn: Boolean)
@Composable
expect fun ObserveWindowFocusChange(onFocusChange: (Boolean) -> Unit)