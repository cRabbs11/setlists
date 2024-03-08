package com.kochkov.evgeny.setlist_mobile.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily

val md_theme_light_primary = Color(0xFF825500)
val md_theme_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_light_primaryContainer = Color(0xFFFFDDB3)
val md_theme_light_onPrimaryContainer = Color(0xFF291800)
val md_theme_light_secondary = Color(0xFF6F5B40)
val md_theme_light_onSecondary = Color(0xFFFFFFFF)
val md_theme_light_secondaryContainer = Color(0xFFFBDEBC)
val md_theme_light_onSecondaryContainer = Color(0xFF271904)
val md_theme_light_tertiary = Color(0xFF51643F)
val md_theme_light_onTertiary = Color(0xFFFFFFFF)
val md_theme_light_tertiaryContainer = Color(0xFFD4EABB)
val md_theme_light_onTertiaryContainer = Color(0xFF102004)
val md_theme_light_error = Color(0xFFBA1A1A)
val md_theme_light_errorContainer = Color(0xFFFFDAD6)
val md_theme_light_onError = Color(0xFFFFFFFF)
val md_theme_light_onErrorContainer = Color(0xFF410002)
val md_theme_light_background = Color(0xFFFFFBFF)
val md_theme_light_onBackground = Color(0xFF1F1B16)
val md_theme_light_surface = Color(0xFFFFFBFF)
val md_theme_light_onSurface = Color(0xFF1F1B16)
val md_theme_light_surfaceVariant = Color(0xFFF0E0CF)
val md_theme_light_onSurfaceVariant = Color(0xFF4F4539)
val md_theme_light_outline = Color(0xFF817567)
val md_theme_light_inverseOnSurface = Color(0xFFF9EFE7)
val md_theme_light_inverseSurface = Color(0xFF34302A)
val md_theme_light_inversePrimary = Color(0xFFFFB951)
val md_theme_light_shadow = Color(0xFF000000)
val md_theme_light_surfaceTint = Color(0xFF825500)
val md_theme_light_outlineVariant = Color(0xFFD3C4B4)
val md_theme_light_scrim = Color(0xFF000000)

val md_theme_green_light_primary = Color(0xFF0ACF00)
val md_theme_green_light_onPrimary = Color(0xFFFFFFFF)
val md_theme_green_light_primaryContainer = Color(0xFFE7B868)
val md_theme_green_light_onPrimaryContainer = Color(0xFF162210)
val md_theme_green_light_secondary = Color(0xFFFF7100)
val md_theme_green_light_onSecondary = Color(0xFF3E2D16)
val md_theme_green_light_secondaryContainer = Color(0xFF56442A)
val md_theme_green_light_onSecondaryContainer = Color(0xFFFBDEBC)
val md_theme_green_light_tertiary = Color(0xFFB8CEA1)
val md_theme_green_light_onTertiary = Color(0xFF243515)
val md_theme_green_light_tertiaryContainer = Color(0xFF3A4C2A)
val md_theme_green_light_onTertiaryContainer = Color(0xFFD4EABB)
val md_theme_green_light_error = Color(0xFFFFB4AB)
val md_theme_green_light_errorContainer = Color(0xFF93000A)
val md_theme_green_light_onError = Color(0xFF690005)
val md_theme_green_light_onErrorContainer = Color(0xFFFFDAD6)
val md_theme_green_light_background = Color(0xFF1F1B16)
val md_theme_green_light_onBackground = Color(0xFFEAE1D9)
val md_theme_green_light_surface = Color(0xFFC7FFAB)
val md_theme_green_light_onSurface = Color(0xFFEAE1D9)
val md_theme_green_light_surfaceVariant = Color(0xFF4F4539)
val md_theme_green_light_onSurfaceVariant = Color(0xFFD3C4B4)
val md_theme_green_light_outline = Color(0xFF9C8F80)
val md_theme_green_light_inverseOnSurface = Color(0xFF1F1B16)
val md_theme_green_light_inverseSurface = Color(0xFFEAE1D9)
val md_theme_green_light_inversePrimary = Color(0xFF825500)
val md_theme_green_light_shadow = Color(0xFF000000)
val md_theme_green_light_surfaceTint = Color(0xFFFFB951)
val md_theme_green_light_outlineVariant = Color(0xFF4F4539)
val md_theme_green_light_scrim = Color(0xFF000000)





private val GreenColors = lightColorScheme(
    primary = md_theme_green_light_primary,
    onPrimary = md_theme_green_light_onPrimary,
    primaryContainer = md_theme_green_light_primaryContainer,
    onPrimaryContainer = md_theme_green_light_onPrimaryContainer,
    secondary = md_theme_green_light_secondary,
    onSecondary = md_theme_green_light_onSecondary,
    secondaryContainer = md_theme_green_light_secondaryContainer,
    onSecondaryContainer = md_theme_green_light_onSecondaryContainer,
    tertiary = md_theme_green_light_tertiary,
    onTertiary = md_theme_green_light_onTertiary,
    tertiaryContainer = md_theme_green_light_tertiaryContainer,
    onTertiaryContainer = md_theme_green_light_onTertiaryContainer,
    error = md_theme_green_light_error,
    errorContainer = md_theme_green_light_errorContainer,
    onError = md_theme_green_light_onError,
    onErrorContainer = md_theme_green_light_onErrorContainer,
    //background = md_theme_green_light_background,
    //onBackground = md_theme_green_light_onBackground,
    surface = md_theme_green_light_surface,
    //onSurface = md_theme_green_light_onSurface,
    //surfaceVariant = md_theme_green_light_surfaceVariant,
    //onSurfaceVariant = md_theme_green_light_onSurfaceVariant,
    //outline = md_theme_green_light_outline,
    //inverseOnSurface = md_theme_green_light_inverseOnSurface,
    //inverseSurface = md_theme_green_light_inverseSurface,
    //inversePrimary = md_theme_green_light_inversePrimary,
    //surfaceTint = md_theme_green_light_surfaceTint,
    //outlineVariant = md_theme_green_light_outlineVariant,
    //scrim = md_theme_green_light_scrim,
)


@Immutable
data class ColorSystem(
    val color: Color,
    val gradient: List<Color>
    /* ... */
)

@Immutable
data class TypographySystem(
    val fontFamily: FontFamily,
    val textStyle: TextStyle
)
/* ... */

@Immutable
data class CustomSystem(
    val value1: Int,
    val value2: String
    /* ... */
)

val LocalColorSystem = staticCompositionLocalOf {
    ColorSystem(
        color = Color.Unspecified,
        gradient = emptyList()
    )
}

val LocalTypographySystem = staticCompositionLocalOf {
    TypographySystem(
        fontFamily = FontFamily.Default,
        textStyle = TextStyle.Default
    )
}

val LocalCustomSystem = staticCompositionLocalOf {
    CustomSystem(
        value1 = 0,
        value2 = ""
    )
}



@Composable
fun MyTheme(
    //useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    //val colors = if (!useDarkTheme) {
    //    LightColors
    //} else {
    //    DarkColors
    //}
    MaterialTheme(
        colorScheme = GreenColors,
        content = content
    )


    /* ... */
    //CompositionLocalProvider(
    //    //LocalColorSystem provides colorSystem,
    //    //LocalTypographySystem provides typographySystem,
    //    //LocalCustomSystem provides customSystem,
    //    /* ... */
    //    content = content
    //)
}

