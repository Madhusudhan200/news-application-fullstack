package com.naatvtelugu.news.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Palette tailored for NAA TV Telugu
val Navy = Color(0xFF0B1426)
val Gold = Color(0xFFF5A623)
val GoldDark = Color(0xFFD48806)
val RedAccent = Color(0xFFE63946)

// Light theme (clean white cards, high-contrast crisp text)
val LightScreenBg = Color(0xFFF4F5F8)
val LightCardBg = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF111827)
val TextSecondary = Color(0xFF374151)
val TextMuted = Color(0xFF6B7280)
val BorderLight = Color(0xFFE5E7EB)

// Dark theme
val DarkScreenBg = Color(0xFF0F141C)
val DarkCardBg = Color(0xFF1A212D)
val DarkTextPrimary = Color(0xFFF9FAFB)
val DarkTextSecondary = Color(0xFFD1D5DB)
val DarkTextMuted = Color(0xFF9CA3AF)
val DarkBorder = Color(0xFF2D3748)

private val LightColors = lightColorScheme(
    primary = GoldDark,
    onPrimary = Color.White,
    secondary = RedAccent,
    onSecondary = Color.White,
    background = LightScreenBg,
    onBackground = TextPrimary,
    surface = LightCardBg,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = TextSecondary,
    surfaceContainer = LightCardBg,
    outline = BorderLight,
    outlineVariant = Color(0xFFEDEDED),
)

private val DarkColors = darkColorScheme(
    primary = Gold,
    onPrimary = Navy,
    secondary = RedAccent,
    onSecondary = Color.White,
    background = DarkScreenBg,
    onBackground = DarkTextPrimary,
    surface = DarkCardBg,
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFF252D3C),
    onSurfaceVariant = DarkTextSecondary,
    surfaceContainer = DarkCardBg,
    outline = DarkBorder,
    outlineVariant = Color(0xFF222B38),
)

private val AppTypography = Typography(
    headlineSmall = TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 15.5.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.15.sp
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 0.5.sp
    ),
)

@Composable
fun NaaTvTheme(dark: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = AppTypography,
        content = content,
    )
}

/** Accent that reads well on either background. */
val accentFor: (Boolean) -> Color = { dark -> if (dark) Gold else GoldDark }
