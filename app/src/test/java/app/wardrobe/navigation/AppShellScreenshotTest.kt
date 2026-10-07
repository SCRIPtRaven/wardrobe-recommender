package app.wardrobe.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import app.wardrobe.AppContainer
import app.wardrobe.testing.captureInTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class AppShellScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun light() = composeRule.captureInTheme { WardrobeApp(AppContainer()) }

    @Test
    fun dark() = composeRule.captureInTheme(darkTheme = true) { WardrobeApp(AppContainer()) }
}
