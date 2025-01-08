package com.ainsln.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

// Default Material 3 typography values
val baseline = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = FontFamily.Serif),
    displayMedium = baseline.displayMedium.copy(fontFamily = FontFamily.Serif),
    displaySmall = baseline.displaySmall.copy(fontFamily = FontFamily.Serif),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = FontFamily.Serif),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = FontFamily.Serif),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = FontFamily.Serif),
    titleLarge = baseline.titleLarge.copy(fontFamily = FontFamily.Serif),
    titleMedium = baseline.titleMedium.copy(fontFamily = FontFamily.Serif),
    titleSmall = baseline.titleSmall.copy(fontFamily = FontFamily.Serif),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = FontFamily.Serif),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = FontFamily.Serif),
    bodySmall = baseline.bodySmall.copy(fontFamily = FontFamily.Serif),
    labelLarge = baseline.labelLarge.copy(fontFamily = FontFamily.Serif),
    labelMedium = baseline.labelMedium.copy(fontFamily = FontFamily.Serif),
    labelSmall = baseline.labelSmall.copy(fontFamily = FontFamily.Serif)
)
