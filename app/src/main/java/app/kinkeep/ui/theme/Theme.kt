package app.kinkeep.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = GreenDeep,
    onPrimary = OnGreenDeep,
    primaryContainer = GreenContainerLight,
    onPrimaryContainer = OnGreenContainerLight,
    inversePrimary = GreenSoft,
    secondary = SecondaryLight,
    onSecondary = Color.White,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = Color.White,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    surfaceTint = GreenDeep,
    inverseSurface = Color(0xFF24342C),
    inverseOnSurface = OnSurfaceDark,
    error = WarmAccentLight,
    onError = Color.White,
    errorContainer = WarmAccentContainerLight,
    onErrorContainer = OnWarmAccentContainerLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    scrim = Color.Black,
    surfaceBright = Color.White,
    surfaceDim = Color(0xFFDCE5E0),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = BackgroundLight,
    surfaceContainer = Color(0xFFEFF5F2),
    surfaceContainerHigh = SurfaceVariantLight,
    surfaceContainerHighest = Color(0xFFDDE9E3),
)

private val DarkColors = darkColorScheme(
    primary = GreenSoft,
    onPrimary = OnGreenSoft,
    primaryContainer = GreenDeep,
    onPrimaryContainer = GreenContainerLight,
    inversePrimary = GreenDeep,
    secondary = SecondaryDark,
    onSecondary = OnGreenSoft,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = GreenContainerLight,
    tertiary = TertiaryDark,
    onTertiary = OnGreenSoft,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = GreenContainerLight,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    surfaceTint = GreenSoft,
    inverseSurface = OnSurfaceDark,
    inverseOnSurface = Color(0xFF24342C),
    error = WarmAccentDark,
    onError = OnWarmAccentDark,
    errorContainer = WarmAccentContainerDark,
    onErrorContainer = OnWarmAccentContainerDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    scrim = Color.Black,
    surfaceBright = Color(0xFF2B3B33),
    surfaceDim = Color(0xFF0E1713),
    surfaceContainerLowest = Color(0xFF09100D),
    surfaceContainerLow = Color(0xFF121D18),
    surfaceContainer = Color(0xFF16221C),
    surfaceContainerHigh = Color(0xFF1C2B24),
    surfaceContainerHighest = SurfaceVariantDark,
)

/** Colors for the relationship health categories. Always shown together with an icon and a label. */
@Immutable
data class HealthColors(
    val thriving: Color,
    val healthy: Color,
    val needsAttention: Color,
    val fading: Color,
    val dormant: Color,
    val fresh: Color,
)

private val LightHealthColors = HealthColors(
    thriving = ThrivingLight,
    healthy = HealthyLight,
    needsAttention = NeedsAttentionLight,
    fading = FadingLight,
    dormant = DormantLight,
    fresh = FreshLight,
)

private val DarkHealthColors = HealthColors(
    thriving = ThrivingDark,
    healthy = HealthyDark,
    needsAttention = NeedsAttentionDark,
    fading = FadingDark,
    dormant = DormantDark,
    fresh = FreshDark,
)

val LocalHealthColors = staticCompositionLocalOf { LightHealthColors }

@Composable
fun KinKeepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalHealthColors provides if (darkTheme) DarkHealthColors else LightHealthColors,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            content = content,
        )
    }
}
