package app.wardrobe.feature.outfits

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OutfitGeneratorViewModelTest {

    private val today = LocalDate.of(2026, 10, 8)
    private lateinit var repository: InMemoryWardrobeRepository
    private lateinit var viewModel: OutfitGeneratorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = InMemoryWardrobeRepository(SampleData.items)
        viewModel = OutfitGeneratorViewModel(repository, SampleData.forecast(today))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun ready() = viewModel.uiState.filterIsInstance<OutfitGeneratorUiState.Ready>().first()

    @Test
    fun startsOnTodayWithNoItemChosen() = runTest {
        val state = ready()
        assertEquals(today, state.selectedDay.date)
        assertNull(state.selectedItem)
        assertFalse(state.canShowOutfits)
    }

    @Test
    fun listsTheWardrobeGroupedByCategory() = runTest {
        val items = ready().items
        assertEquals(SampleData.items.size, items.size)
        assertEquals(items.sortedBy { it.category }, items)
    }

    @Test
    fun choosingAnItemAllowsShowingOutfits() = runTest {
        viewModel.selectItem("dark-jeans")
        val state = ready()
        assertEquals("dark-jeans", state.selectedItem?.id)
        assertTrue(state.canShowOutfits)
    }

    @Test
    fun choosingADayShowsItsWeather() = runTest {
        viewModel.selectDay(today.plusDays(1))
        assertEquals(today.plusDays(1), ready().selectedDay.date)
    }

    @Test
    fun deletingTheChosenItemClearsTheChoice() = runTest {
        viewModel.selectItem("dark-jeans")
        repository.delete("dark-jeans")
        assertNull(ready().selectedItem)
        assertFalse(ready().canShowOutfits)
    }
}
