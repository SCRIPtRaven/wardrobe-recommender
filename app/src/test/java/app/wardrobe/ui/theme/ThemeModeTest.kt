package app.wardrobe.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun systemFollowsTheSystemSetting() {
        assertEquals(true, ThemeMode.SYSTEM.isDark(systemInDarkTheme = true))
        assertEquals(false, ThemeMode.SYSTEM.isDark(systemInDarkTheme = false))
    }

    @Test
    fun lightAndDarkIgnoreTheSystemSetting() {
        assertEquals(false, ThemeMode.LIGHT.isDark(systemInDarkTheme = true))
        assertEquals(true, ThemeMode.DARK.isDark(systemInDarkTheme = false))
    }
}
