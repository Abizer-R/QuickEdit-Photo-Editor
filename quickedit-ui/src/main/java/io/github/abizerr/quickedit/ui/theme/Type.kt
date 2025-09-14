package io.github.abizerr.quickedit.ui.theme

import androidx.compose.material3.Typography
import io.github.abizerr.quickedit.ui.utils.font.FontUtils

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = Typography().displayLarge.copy(fontFamily = FontUtils.DefaultFontFamily),
    displayMedium = Typography().displayMedium.copy(fontFamily = FontUtils.DefaultFontFamily),
    displaySmall = Typography().displaySmall.copy(fontFamily = FontUtils.DefaultFontFamily),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = FontUtils.DefaultFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = FontUtils.DefaultFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = FontUtils.DefaultFontFamily),
    titleLarge = Typography().titleLarge.copy(fontFamily = FontUtils.DefaultFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = FontUtils.DefaultFontFamily),
    titleSmall = Typography().titleSmall.copy(fontFamily = FontUtils.DefaultFontFamily),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = FontUtils.DefaultFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = FontUtils.DefaultFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = FontUtils.DefaultFontFamily),
    labelLarge = Typography().labelLarge.copy(fontFamily = FontUtils.DefaultFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = FontUtils.DefaultFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = FontUtils.DefaultFontFamily),
)