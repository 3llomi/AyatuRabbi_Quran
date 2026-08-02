package com.devlomi.shared.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
//    val amiriFontFamily = FontFamily(Font(resource = Res.font.amiri, weight = FontWeight.Normal))
//    val notoSansArabic = FontFamily(Font(resource = Res.font.noto_hinted, weight = FontWeight.Normal))

//    val colorScheme = if (isSystemInDarkTheme()) hadithDarkColorScheme else hadithLightColorScheme

    MaterialTheme(
//        colorScheme = colorScheme,
//        typography = Typography().withRoleFonts(
//            headlineFamily = amiriFontFamily,
//            bodyFamily = notoSansArabic,
//            labelFamily = notoSansArabic,
//        ),
    ) {
        content()
    }
}