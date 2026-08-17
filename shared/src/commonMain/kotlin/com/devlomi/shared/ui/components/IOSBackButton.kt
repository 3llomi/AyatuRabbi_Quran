package com.devlomi.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devlomi.shared.platform

@Composable
fun IOSBackButton(modifier: Modifier, onBackPressed: () -> Unit) {
    if (platform() != "iOS") {
        return
    }
    IconButton(modifier = modifier, onClick = onBackPressed) {
        // You can use an icon here, for example:
        Icon(
            imageVector = Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}

@Preview
@Composable
fun IOSBackButtonPreview() {
    IOSBackButton(
        modifier = Modifier.size(48.dp)
            .padding(top = 16.dp, end = 16.dp), onBackPressed = {})
}