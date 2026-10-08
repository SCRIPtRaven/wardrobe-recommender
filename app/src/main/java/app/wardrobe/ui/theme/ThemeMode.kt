package app.wardrobe.ui.theme

/** The theme the user picked in Settings. */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    ;

    /** Whether the app uses its dark color scheme, given the system's dark theme setting. */
    fun isDark(systemInDarkTheme: Boolean): Boolean = when (this) {
        SYSTEM -> systemInDarkTheme
        LIGHT -> false
        DARK -> true
    }
}
