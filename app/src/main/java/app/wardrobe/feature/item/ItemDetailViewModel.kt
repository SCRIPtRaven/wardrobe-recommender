package app.wardrobe.feature.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.ClothingItem
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

sealed interface ItemDetailUiState {
    data object Loading : ItemDetailUiState
    data object NotFound : ItemDetailUiState
    data class Loaded(val item: ClothingItem) : ItemDetailUiState
}

class ItemDetailViewModel(
    private val itemId: String,
    private val repository: WardrobeRepository,
) : ViewModel() {

    val uiState: StateFlow<ItemDetailUiState> =
        repository.item(itemId)
            .runningFold<ClothingItem?, ItemDetailUiState>(ItemDetailUiState.Loading) { previous, item ->
                when {
                    item != null -> ItemDetailUiState.Loaded(item)
                    // Deleted while open: keep showing it while the screen animates away.
                    previous is ItemDetailUiState.Loaded -> previous
                    else -> ItemDetailUiState.NotFound
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ItemDetailUiState.Loading)

    /** Deletes the item and returns it so the caller can offer undo. Null if it was never loaded. */
    suspend fun delete(): ClothingItem? {
        val item = (uiState.value as? ItemDetailUiState.Loaded)?.item ?: return null
        repository.delete(itemId)
        return item
    }
}
