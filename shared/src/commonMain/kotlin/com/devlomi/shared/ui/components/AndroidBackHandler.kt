package com.devlomi.shared.ui.components

import androidx.compose.runtime.Composable

@Composable
expect fun AndroidBackHandler(enabled: Boolean = true, onBack: () -> Unit)