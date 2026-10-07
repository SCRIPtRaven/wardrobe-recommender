package app.wardrobe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily

private val Base = Typography()

/**
 * Serif headings over the default sans-serif body text, for an editorial look.
 * [FontFamily.Serif] is the system serif font, so no font files ship with the app.
 */
internal val WardrobeTypography = Typography(
    displayLarge = Base.displayLarge.copy(fontFamily = FontFamily.Serif),
    displayMedium = Base.displayMedium.copy(fontFamily = FontFamily.Serif),
    displaySmall = Base.displaySmall.copy(fontFamily = FontFamily.Serif),
    headlineLarge = Base.headlineLarge.copy(fontFamily = FontFamily.Serif),
    headlineMedium = Base.headlineMedium.copy(fontFamily = FontFamily.Serif),
    headlineSmall = Base.headlineSmall.copy(fontFamily = FontFamily.Serif),
    titleLarge = Base.titleLarge.copy(fontFamily = FontFamily.Serif),
)
