package app.wardrobe.feature.outfits

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import app.wardrobe.domain.model.Category
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OutfitResultsViewModelTest {

    private val today = LocalDate.of(2026, 10, 8)
    private val forecast = SampleData.forecast(today)
    private lateinit var repository: InMemoryWardrobeRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = InMemoryWardrobeRepository(SampleData.items)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(itemId: String, date: LocalDate = today) =
        OutfitResultsViewModel(itemId, date, repository, forecast)

    private suspend fun OutfitResultsViewModel.settled() = uiState.first { it != OutfitResultsUiState.Loading }

    private suspend fun OutfitResultsViewModel.ready() = settled() as OutfitResultsUiState.Ready

    @Test
    fun theSampleAnchorGetsTheThreeSampleOutfits() = runTest {
        val state = viewModel(SampleOutfits.ANCHOR_ITEM_ID).ready()
        assertEquals(SampleOutfits.ANCHOR_ITEM_ID, state.anchor.id)
        assertEquals(SampleOutfits.all.map { it.id }, state.outfits.map { it.id })
    }

    @Test
    fun outfitItemsShowOuterLayerFirstAndShoesLast() = runTest {
        val smartCasual = viewModel(SampleOutfits.ANCHOR_ITEM_ID).ready().outfits.first()
        assertEquals(
            listOf(Category.OUTERWEAR, Category.TOP, Category.BOTTOM, Category.FOOTWEAR),
            smartCasual.items.map { it.category },
        )
    }

    @Test
    fun otherItemsGetNoOutfitsYet() = runTest {
        val state = viewModel("black-tee").ready()
        assertEquals("black-tee", state.anchor.id)
        assertTrue(state.outfits.isEmpty())
    }

    @Test
    fun aMissingItemIsNotFound() = runTest {
        assertEquals(OutfitResultsUiState.NotFound, viewModel("missing").settled())
    }

    @Test
    fun showsTheWeatherOfTheChosenDay() = runTest {
        assertEquals(today.plusDays(1), viewModel(SampleOutfits.ANCHOR_ITEM_ID, today.plusDays(1)).ready().day.date)
    }

    @Test
    fun outfitsWithADeletedItemDropOut() = runTest {
        repository.delete("brown-boots")
        val ids = viewModel(SampleOutfits.ANCHOR_ITEM_ID).ready().outfits.map { it.id }
        assertEquals(listOf("easy-weekend"), ids)
    }

    @Test
    fun ratingAgainClearsItAndTheOtherRatingReplacesIt() = runTest {
        val viewModel = viewModel(SampleOutfits.ANCHOR_ITEM_ID)
        viewModel.rate("rainy-day", Rating.LIKED)
        assertEquals(Rating.LIKED, viewModel.ready().outfits.first { it.id == "rainy-day" }.rating)

        viewModel.rate("rainy-day", Rating.DISLIKED)
        assertEquals(Rating.DISLIKED, viewModel.ready().outfits.first { it.id == "rainy-day" }.rating)

        viewModel.rate("rainy-day", Rating.DISLIKED)
        assertNull(viewModel.ready().outfits.first { it.id == "rainy-day" }.rating)
    }
}
