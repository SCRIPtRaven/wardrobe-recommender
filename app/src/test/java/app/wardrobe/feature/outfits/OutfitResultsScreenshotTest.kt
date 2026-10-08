package app.wardrobe.feature.outfits

import androidx.compose.ui.test.junit4.createComposeRule
import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import app.wardrobe.testing.captureInTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel7)
class OutfitResultsScreenshotTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val today = LocalDate.of(2026, 10, 8)

    /** The state the real view model builds, so the screenshots show the real sample outfits. */
    private fun state(itemId: String, ratings: Map<String, Rating> = emptyMap()): OutfitResultsUiState = runBlocking {
        val viewModel = OutfitResultsViewModel(
            itemId = itemId,
            date = today.plusDays(1),
            repository = InMemoryWardrobeRepository(SampleData.items),
            forecast = SampleData.forecast(today),
        )
        ratings.forEach { (id, rating) -> viewModel.rate(id, rating) }
        viewModel.uiState.first { it is OutfitResultsUiState.Ready }
    }

    @Test
    fun outfits() = composeRule.captureInTheme {
        OutfitResultsContent(state(SampleOutfits.ANCHOR_ITEM_ID), onBack = {}, onRate = { _, _ -> })
    }

    @Test
    fun outfitsDark() = composeRule.captureInTheme(darkTheme = true) {
        OutfitResultsContent(state(SampleOutfits.ANCHOR_ITEM_ID), onBack = {}, onRate = { _, _ -> })
    }

    @Test
    fun rated() = composeRule.captureInTheme {
        val state = state(SampleOutfits.ANCHOR_ITEM_ID, mapOf("smart-casual" to Rating.LIKED))
        OutfitResultsContent(state, onBack = {}, onRate = { _, _ -> })
    }

    @Test
    fun noOutfitsForThisItem() = composeRule.captureInTheme {
        OutfitResultsContent(state("black-tee"), onBack = {}, onRate = { _, _ -> })
    }
}
