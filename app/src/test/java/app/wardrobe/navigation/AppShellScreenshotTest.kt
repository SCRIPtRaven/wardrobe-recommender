package app.wardrobe.navigation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
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
    fun light() = capture(darkTheme = false)

    @Test
    fun dark() = capture(darkTheme = true)

    private fun capture(darkTheme: Boolean) {
        composeRule.setContent { WardrobeTheme(darkTheme = darkTheme) { WardrobeApp() } }
        composeRule.onRoot().captureRoboImage()
    }
}
