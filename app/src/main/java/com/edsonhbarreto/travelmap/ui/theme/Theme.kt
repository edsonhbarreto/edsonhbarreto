package com.edsonhbarreto.travelmap.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Vibrant "sunset over the ocean" palette for a travel app.
val Sunset = Color(0xFFFF6F3C)
val Turquoise = Color(0xFF00BFA6)
val SunYellow = Color(0xFFFFC93C)
val DeepBlue = Color(0xFF1B3A57)
val Cream = Color(0xFFFFF8F0)
val CoralLight = Color(0xFFFFE0D2)

/** Distinct, cheerful colors auto-assigned to places so each gets its own identity on the map/cards. */
val PlaceColors = listOf(
    Color(0xFFFF6F3C), // sunset orange
    Color(0xFF00BFA6), // turquoise
    Color(0xFF9B5DE5), // violet
    Color(0xFFFFC93C), // sun yellow
    Color(0xFFEF476F), // pink/red
    Color(0xFF06D6A0), // mint
    Color(0xFF4361EE), // blue
    Color(0xFFF77F00), // amber
)

private val LightColors = lightColorScheme(
    primary = Sunset,
    onPrimary = Color.White,
    secondary = Turquoise,
    onSecondary = Color.White,
    tertiary = SunYellow,
    onTertiary = DeepBlue,
    background = Cream,
    onBackground = DeepBlue,
    surface = Color.White,
    onSurface = DeepBlue,
    surfaceVariant = CoralLight,
)

private val DarkColors = darkColorScheme(
    primary = Sunset,
    onPrimary = Color.White,
    secondary = Turquoise,
    onSecondary = Color.White,
    tertiary = SunYellow,
    onTertiary = DeepBlue,
    background = Color(0xFF161B22),
    onBackground = Color(0xFFF2F2F2),
    surface = Color(0xFF1F2733),
    onSurface = Color(0xFFF2F2F2),
)

private val AppTypography = Typography(
    titleLarge = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    bodyLarge = TextStyle(fontSize = 16.sp),
)

@Composable
fun TravelMapTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
