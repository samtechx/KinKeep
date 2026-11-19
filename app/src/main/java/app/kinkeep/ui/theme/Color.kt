package app.kinkeep.ui.theme

import androidx.compose.ui.graphics.Color

// Palette: dark green + white, with neutral green-tinted greys. One warm accent (Fading / error).
// Contrast ratios for text pairs were computed against WCAG AA (4.5:1) and all pass.

// ---- Light theme ----
val GreenDeep = Color(0xFF0B3D2E)
val OnGreenDeep = Color(0xFFFFFFFF)
val GreenContainerLight = Color(0xFFCDE8DC)
val OnGreenContainerLight = Color(0xFF062219)
val BackgroundLight = Color(0xFFF6FAF8)
val SurfaceLight = Color(0xFFFFFFFF)
val OnSurfaceLight = Color(0xFF10201A)
val SurfaceVariantLight = Color(0xFFE6F0EB)
val OnSurfaceVariantLight = Color(0xFF3F5249)
val OutlineLight = Color(0xFF6F8277)
val OutlineVariantLight = Color(0xFFC3D3CA)
val SecondaryLight = Color(0xFF2E7D57)
val SecondaryContainerLight = Color(0xFFDCEFE4)
val OnSecondaryContainerLight = Color(0xFF0F2E21)
val TertiaryLight = Color(0xFF1F5C47)
val TertiaryContainerLight = Color(0xFFD6EBE0)
val OnTertiaryContainerLight = Color(0xFF0B2A1F)

// ---- Dark theme ----
val GreenSoft = Color(0xFF7FD1AE)
val OnGreenSoft = Color(0xFF00281B)
val BackgroundDark = Color(0xFF0E1713)
val SurfaceDark = Color(0xFF0E1713)
val OnSurfaceDark = Color(0xFFE3EEE8)
val SurfaceVariantDark = Color(0xFF24342C)
val OnSurfaceVariantDark = Color(0xFFB7C9BF)
val OutlineDark = Color(0xFF7F9388)
val OutlineVariantDark = Color(0xFF3A4D43)
val SecondaryDark = Color(0xFF8FC9AB)
val SecondaryContainerDark = Color(0xFF1E3A2F)
val TertiaryDark = Color(0xFF5FD0A0)
val TertiaryContainerDark = Color(0xFF1A3A2D)

// ---- The single warm accent (used for "Fading" and for errors) ----
val WarmAccentLight = Color(0xFFB45309)
val WarmAccentContainerLight = Color(0xFFFBE3CE)
val OnWarmAccentContainerLight = Color(0xFF4A2200)
val WarmAccentDark = Color(0xFFF2A65A)
val OnWarmAccentDark = Color(0xFF3A1B00)
val WarmAccentContainerDark = Color(0xFF5A2E00)
val OnWarmAccentContainerDark = Color(0xFFFFDDBA)

// ---- Health category colors ----
val ThrivingLight = Color(0xFF0B6B4A)
val HealthyLight = Color(0xFF2E7D57)
val NeedsAttentionLight = Color(0xFF5B7F2B)
val FadingLight = WarmAccentLight
val DormantLight = Color(0xFF5E6F66)
val FreshLight = OnSurfaceVariantLight

val ThrivingDark = Color(0xFF5FD0A0)
val HealthyDark = Color(0xFF7FD1AE)
val NeedsAttentionDark = Color(0xFFB5D66F)
val FadingDark = WarmAccentDark
val DormantDark = Color(0xFF9DB0A6)
val FreshDark = OnSurfaceVariantDark
