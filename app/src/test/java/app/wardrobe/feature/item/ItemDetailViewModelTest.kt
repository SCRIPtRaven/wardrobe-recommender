package app.wardrobe.feature.item

import app.wardrobe.data.InMemoryWardrobeRepository
import app.wardrobe.data.SampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ItemDetailViewModelTest {

    private val tee = SampleData.items.first { it.id == "black-tee" }
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

    private suspend fun ItemDetailViewModel.settledState() =
        uiState.first { it != ItemDetailUiState.Loading }

    @Test
    fun loadsTheItem() = runTest {
        val viewModel = ItemDetailViewModel(tee.id, repository)
        assertEquals(ItemDetailUiState.Loaded(tee), viewModel.settledState())
    }

    @Test
    fun unknownIdIsNotFound() = runTest {
        val viewModel = ItemDetailViewModel("missing", repository)
        assertEquals(ItemDetailUiState.NotFound, viewModel.settledState())
    }

    @Test
    fun deleteRemovesTheItemAndReturnsIt() = runTest {
        val viewModel = ItemDetailViewModel(tee.id, repository)
        viewModel.settledState()
        assertEquals(tee, viewModel.delete())
        assertNull(repository.item(tee.id).first())
    }

    @Test
    fun keepsShowingTheItemAfterItIsDeleted() = runTest {
        // The screen stays visible during the exit animation, so it must not flash "not found".
        val viewModel = ItemDetailViewModel(tee.id, repository)
        val collected = mutableListOf<ItemDetailUiState>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect { collected += it }
        }
        assertEquals(tee, viewModel.delete())
        assertEquals(ItemDetailUiState.Loaded(tee), collected.last())
        assertFalse(ItemDetailUiState.NotFound in collected)
    }
}
