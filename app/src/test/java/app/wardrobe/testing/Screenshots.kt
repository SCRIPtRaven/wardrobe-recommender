package app.wardrobe.testing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Density
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.captureScreenRoboImage

/**
 * Renders [content] in the app theme and captures it under the calling test's name.
 * [fontScale] works like the system font size setting, so 2f checks text at 200%.
 */
fun ComposeContentTestRule.captureInTheme(
    darkTheme: Boolean = false,
    fontScale: Float = 1f,
    content: @Composable () -> Unit,
) {
    setContent {
        val density = LocalDensity.current
        CompositionLocalProvider(LocalDensity provides Density(density.density, fontScale)) {
            WardrobeTheme(darkTheme = darkTheme, content = content)
        }
    }
    onRoot().captureRoboImage()
}

/** Like [captureInTheme], but captures every window, so dialogs and bottom sheets are included. */
fun ComposeContentTestRule.captureScreenInTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    setContent { WardrobeTheme(darkTheme = darkTheme, content = content) }
    waitForIdle()
    captureScreenRoboImage()
}
