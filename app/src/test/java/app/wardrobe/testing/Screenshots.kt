package app.wardrobe.testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.captureRoboImage
import com.github.takahirom.roborazzi.captureScreenRoboImage

/** Renders [content] in the app theme and captures it under the calling test's name. */
fun ComposeContentTestRule.captureInTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    setContent { WardrobeTheme(darkTheme = darkTheme, content = content) }
    onRoot().captureRoboImage()
}

/** Like [captureInTheme], but captures every window, so dialogs and bottom sheets are included. */
fun ComposeContentTestRule.captureScreenInTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    setContent { WardrobeTheme(darkTheme = darkTheme, content = content) }
    waitForIdle()
    captureScreenRoboImage()
}
