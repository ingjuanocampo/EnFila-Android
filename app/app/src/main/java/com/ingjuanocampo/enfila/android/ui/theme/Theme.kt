package com.ingjuanocampo.enfila.android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


private val LightColors = lightColorScheme(
    surfaceTint = md_theme_light_surfaceTint,
    onErrorContainer = md_theme_light_onErrorContainer,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    tertiary = md_theme_light_tertiary,
    error = md_theme_light_error,
    outline = md_theme_light_outline,
    onBackground = md_theme_light_onBackground,
    background = md_theme_light_background,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    surface = md_theme_light_surface,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    secondary = md_theme_light_secondary,
    inversePrimary = md_theme_light_inversePrimary,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    primary = md_theme_light_primary,
)


private val DarkColors = darkColorScheme(
    surfaceTint = md_theme_dark_surfaceTint,
    onErrorContainer = md_theme_dark_onErrorContainer,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    tertiary = md_theme_dark_tertiary,
    error = md_theme_dark_error,
    outline = md_theme_dark_outline,
    onBackground = md_theme_dark_onBackground,
    background = md_theme_dark_background,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    surface = md_theme_dark_surface,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    secondary = md_theme_dark_secondary,
    inversePrimary = md_theme_dark_inversePrimary,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    primary = md_theme_dark_primary,
)


@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content,
        typography = Typography
    )
}

internal object TextAppStyles {

    @Composable
    fun HeadLineTextStyle() = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 25.sp,
        fontFamily = SansSerif,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun BodyTextMediumStyle() = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 16.sp,
        fontFamily = SansSerif,
        fontWeight = FontWeight.Medium
    )

    @Composable
    fun BodyTextMediumStyleError() = TextStyle(
        color = MaterialTheme.colorScheme.error,
        fontSize = 16.sp,
        fontFamily = SansSerif,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun BodyTextMediumStyleTernary() = TextStyle(
        color = MaterialTheme.colorScheme.tertiary,
        fontSize = 16.sp,
        fontFamily = SansSerif,
        fontWeight = FontWeight.Bold
    )

    @Composable
    fun BodyTextStyle() = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 14.sp,
        fontFamily = SansSerif,
    )

    @Composable
    fun SmallTextStyle() = TextStyle(
        color = MaterialTheme.colorScheme.secondary,
        fontSize = 10.sp,
        fontFamily = SansSerif,
    )
}



