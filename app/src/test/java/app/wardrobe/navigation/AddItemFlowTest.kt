package app.wardrobe.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import app.wardrobe.AppContainer
import app.wardrobe.data.SampleData
import app.wardrobe.data.SampleRecognizer
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Adds an item through the photo flow, and discards one. Recognition answers without delay. */
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class AddItemFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        val container = AppContainer(recognizer = SampleRecognizer(SampleData.photoSuggestions, delayMillis = 0))
        composeRule.setContent { WardrobeTheme { WardrobeApp(container) } }
        composeRule.onNodeWithContentDescription("Add item").performClick()
        composeRule.onNodeWithText("Take a photo").performClick()
    }

    @Test
    fun savedItemAppearsInTheWardrobe() {
        composeRule.onNodeWithText("Suggested from your photo. Change anything that is wrong.").assertIsDisplayed()
        composeRule.onNodeWithText("Autumn").performScrollTo().performClick()
        composeRule.onNodeWithText("Save to wardrobe").performClick()

        composeRule.onNodeWithText("Green sweater added").assertIsDisplayed()
        composeRule.onNodeWithText("Green sweater").assertIsDisplayed()
    }

    @Test
    fun saveWithoutASeasonShowsAnError() {
        composeRule.onNodeWithText("Save to wardrobe").performClick()
        composeRule.onNodeWithText("Pick at least one season").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun discardLeavesTheWardrobeUnchanged() {
        composeRule.onNodeWithContentDescription("Close").performClick()
        composeRule.onNodeWithText("Discard").performClick()

        composeRule.onNodeWithText("Black tee").assertIsDisplayed()
        composeRule.onNodeWithText("Green sweater").assertDoesNotExist()
    }
}
