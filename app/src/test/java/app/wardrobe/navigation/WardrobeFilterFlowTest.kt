package app.wardrobe.navigation

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import app.wardrobe.AppContainer
import app.wardrobe.ui.theme.WardrobeTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/** Narrows the wardrobe grid with the category chips and the filter sheet. */
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class WardrobeFilterFlowTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        composeRule.setContent { WardrobeTheme { WardrobeApp(AppContainer()) } }
    }

    @Test
    fun categoryChipShowsOnlyThatCategory() {
        composeRule.onNodeWithText("Footwear").performClick()
        composeRule.onNodeWithText("Brown boots").assertIsDisplayed()
        composeRule.onNodeWithText("Black tee").assertDoesNotExist()
    }

    @Test
    fun colorFilterNarrowsTheGridUntilCleared() {
        composeRule.onNodeWithContentDescription("Filters").performClick()
        composeRule.onNode(colorChip("Burgundy")).performClick()
        composeRule.onNodeWithText("Done").performClick()

        composeRule.onNodeWithText("Burgundy sweater").assertIsDisplayed()
        composeRule.onNodeWithText("Black tee").assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Filters, some applied").performClick()
        composeRule.onNodeWithText("Clear all").performClick()
        composeRule.onNodeWithText("Done").performClick()
        composeRule.onNodeWithText("Black tee").assertIsDisplayed()
    }

    @Test
    fun filtersWithoutMatchesOfferToClearThem() {
        composeRule.onNodeWithText("Footwear").performClick()
        composeRule.onNodeWithContentDescription("Filters").performClick()
        composeRule.onNode(colorChip("Burgundy")).performClick()
        composeRule.onNodeWithText("Done").performClick()

        composeRule.onNodeWithText("No items match").assertIsDisplayed()
        composeRule.onNodeWithText("Clear filters").performClick()
        composeRule.onNodeWithText("Black tee").assertIsDisplayed()
    }

    /** The filter sheet's chip, not the card whose color label shows the same word. */
    private fun colorChip(color: String) =
        hasText(color) and SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox)
}
