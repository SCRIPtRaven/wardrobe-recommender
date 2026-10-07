package app.wardrobe.feature.wardrobe

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import app.wardrobe.domain.model.Category
import app.wardrobe.domain.model.ItemColor
import app.wardrobe.domain.model.Season
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WardrobeViewModelTest {

    private lateinit var repository: InMemoryWardrobeRepository
    private lateinit var viewModel: WardrobeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = InMemoryWardrobeRepository(SampleData.items)
        viewModel = WardrobeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun loaded() = viewModel.uiState.filterIsInstance<WardrobeUiState.Loaded>().first()

    @Test
    fun showsEveryItemGroupedByCategory() = runTest {
        val state = loaded()
        assertEquals(SampleData.items.size, state.items.size)
        assertEquals(state.items.sortedBy { it.category }, state.items)
        assertTrue(state.hasAnyItems)
    }

    @Test
    fun selectingACategoryShowsOnlyThatCategory() = runTest {
        viewModel.selectCategory(Category.FOOTWEAR)
        val state = loaded()
        assertTrue(state.items.isNotEmpty())
        assertTrue(state.items.all { it.category == Category.FOOTWEAR })
    }

    @Test
    fun togglingAColorTwiceRemovesIt() = runTest {
        viewModel.toggleColor(ItemColor.BLACK)
        assertTrue(loaded().items.all { it.color == ItemColor.BLACK })
        viewModel.toggleColor(ItemColor.BLACK)
        assertEquals(SampleData.items.size, loaded().items.size)
    }

    @Test
    fun clearFiltersResetsEverything() = runTest {
        viewModel.selectCategory(Category.TOP)
        viewModel.toggleSeason(Season.SUMMER)
        viewModel.clearFilters()
        val state = loaded()
        assertFalse(state.filter.isActive)
        assertEquals(SampleData.items.size, state.items.size)
    }

    @Test
    fun filterColorsListOnlyColorsInTheWardrobe() = runTest {
        val state = loaded()
        assertEquals(SampleData.items.map { it.color }.toSet(), state.availableColors.toSet())
        assertEquals(state.availableColors.sorted(), state.availableColors)
    }

    @Test
    fun selectedColorStaysInTheSheetAfterItsLastItemIsDeleted() = runTest {
        viewModel.toggleColor(ItemColor.BURGUNDY)
        repository.delete("burgundy-sweater")
        val state = loaded()
        assertTrue(state.items.isEmpty())
        assertTrue(ItemColor.BURGUNDY in state.filter.colors)
        assertTrue(ItemColor.BURGUNDY in state.availableColors)
    }
}
