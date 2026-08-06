package com.devlomi.shared.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ayaturabbi.shared.generated.resources.Res
import ayaturabbi.shared.generated.resources.cairo_black
import ayaturabbi.shared.generated.resources.cairo_bold
import ayaturabbi.shared.generated.resources.cairo_regular
import ayaturabbi.shared.generated.resources.naskh
import org.jetbrains.compose.resources.Font

// Color definitions from colors.xml
private val colorPrimary = Color(0xFF0F3250)
private val colorPrimaryVariant = Color(0xFF0B263C)
private val colorSecondary = Color(0xFF1CAD91)
private val colorSecondaryVariant = Color(0xFF16856F)
private val colorOnPrimary = Color(0xFF98ABBE)
private val colorOnSecondary = Color(0xFFFFFFFF)
private val colorOnBackground = Color(0xFFFFFFFF)
private val colorOnError = Color(0xFFC50D28)
private val colorOnCard = Color(0xFFADADAD)
private val colorOnSurfaceHigh = Color(0xFFFFFFFF)
private val colorOnSurfaceMedium = Color(0xFFD7E1EA)
private val colorBlack = Color(0xFF000000)
private val colorWhite = Color(0xFFFFFFFF)
private val colorDkblue = Color(0xFF0C2942)
private val colorDkgray = Color(0xFF8F8F8F)
private val colorBgPanel = Color(0xFF264B6A)

private val appColorScheme = darkColorScheme(
    primary = colorPrimary,
    onPrimary = colorOnPrimary,
    primaryContainer = colorPrimaryVariant,
    onPrimaryContainer = colorOnPrimary,
    secondary = colorSecondary,
    onSecondary = colorOnSecondary,
    secondaryContainer = colorSecondaryVariant,
    onSecondaryContainer = colorOnSecondary,
    tertiary = colorBgPanel,
    onTertiary = colorOnBackground,
    tertiaryContainer = colorBgPanel,
    onTertiaryContainer = colorOnBackground,
    background = colorDkblue,
    onBackground = colorOnBackground,
    surface = colorBgPanel,
    onSurface = colorOnSurfaceHigh,
    surfaceVariant = colorDkgray,
    onSurfaceVariant = colorOnSurfaceMedium,
    outline = colorOnSurfaceMedium,
    outlineVariant = colorDkgray,
    error = colorOnError,
    onError = colorWhite,
    errorContainer = colorOnError,
    onErrorContainer = colorWhite,
    scrim = colorBlack,
    inverseSurface = colorWhite,
    inverseOnSurface = colorBlack,
    inversePrimary = colorSecondary
)

@Composable
fun cairoFont(): FontFamily {
    return FontFamily(Font(resource = Res.font.cairo_regular, weight = FontWeight.Normal))
}
@Composable
fun AppTheme(content: @Composable () -> Unit) {
     val cairoBold = FontFamily(Font(resource = Res.font.cairo_bold, weight = FontWeight.Bold))
     val cairoBlack = FontFamily(Font(resource = Res.font.cairo_black, weight = FontWeight.Black))
     val cairoRegular = FontFamily(Font(resource = Res.font.cairo_regular, weight = FontWeight.Normal))

     val appTypography = Typography(
        displayLarge = TextStyle(
            fontFamily = cairoBlack,
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = cairoBlack,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = cairoBold,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = cairoBold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = cairoBold,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 26.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = cairoBold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = cairoBold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = cairoBold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = cairoBold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = cairoRegular,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = cairoRegular,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle(
            fontFamily = cairoRegular,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),
        labelLarge = TextStyle(
            fontFamily = cairoBold,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle(
            fontFamily = cairoBold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = cairoBold,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 14.sp,
            letterSpacing = 0.sp
        )
    )


    MaterialTheme(
        colorScheme = appColorScheme,
        typography = appTypography
    ) {
        content()
    }
}