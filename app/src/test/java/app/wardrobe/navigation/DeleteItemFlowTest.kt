package app.wardrobe.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.wardrobe.AppContainer
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Opens an item from the wardrobe grid, deletes it and brings it back with undo. */
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class DeleteItemFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deleteThenUndoRestoresTheItem() {
        composeRule.setContent { WardrobeTheme { WardrobeApp(AppContainer()) } }

        composeRule.onNodeWithText("Black tee").performClick()
        composeRule.onNodeWithText("T-shirt in Tops").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Delete item").performClick()
        composeRule.onNodeWithText("Delete Black tee?").assertIsDisplayed()
        composeRule.onNodeWithText("Delete").performClick()

        composeRule.onNodeWithText("Black tee deleted").assertIsDisplayed()
        composeRule.onNodeWithText("Black tee").assertDoesNotExist()

        composeRule.onNodeWithText("Undo").performClick()
        composeRule.onNodeWithText("Black tee").assertIsDisplayed()
    }

    @Test
    fun cancelKeepsTheItem() {
        composeRule.setContent { WardrobeTheme { WardrobeApp(AppContainer()) } }

        composeRule.onNodeWithText("Black tee").performClick()
        composeRule.onNodeWithContentDescription("Delete item").performClick()
        composeRule.onNodeWithText("Cancel").performClick()

        composeRule.onNodeWithText("Delete Black tee?").assertDoesNotExist()
        composeRule.onNodeWithText("T-shirt in Tops").assertIsDisplayed()
    }
}
