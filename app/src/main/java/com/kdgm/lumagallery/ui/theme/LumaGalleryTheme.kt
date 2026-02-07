package com.kdgm.lumagallery.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(

    primary = AccentBlue,
    error = AccentRed,

    background = Black,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,

    onPrimary = White,
    onBackground = White,
    onSurface = White,
    onSurfaceVariant = White70


)

@Composable
fun LumaGalleryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = GalleryTypography,
        shapes = GalleryShapes,
        content = content
    )
}
