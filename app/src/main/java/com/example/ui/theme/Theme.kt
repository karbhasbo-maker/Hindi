package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = SaffronSecondary,
    onPrimary = Color.White,
    primaryContainer = SaffronDark,
    onPrimaryContainer = Color.White,
    secondary = AmberGold,
    onSecondary = Color.White,
    secondaryContainer = IndicCardDark,
    onSecondaryContainer = Color.White,
    tertiary = SaffronAccent,
    background = IndicDark,
    onBackground = Color(0xFFF1F5F9),
    surface = IndicCardDark,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = IndicSurfaceDark,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = SaffronLight,
    onPrimaryContainer = SaffronDark,
    secondary = SaffronSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmberLight,
    onSecondaryContainer = SaffronDark,
    tertiary = AmberGold,
    background = IndicLightBg,
    onBackground = Color(0xFF1E293B),
    surface = IndicLightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = IndicLightBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep our cohesive Indian saffron theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
