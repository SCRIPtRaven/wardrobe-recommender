package app.wardrobe.navigation

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.wardrobe.AppContainer
import app.wardrobe.ui.theme.ThemeMode
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class SettingsFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun choosingDarkSetsTheAppTheme() {
        val container = AppContainer()
        composeRule.setContent { WardrobeTheme { WardrobeApp(container) } }

        composeRule.onNodeWithText("Settings").performClick()
        composeRule.onNodeWithText("System").assertIsSelected()
        composeRule.onNodeWithText("Dark").performClick()

        composeRule.onNodeWithText("Dark").assertIsSelected()
        assertEquals(ThemeMode.DARK, container.themeMode.value)
    }
}
