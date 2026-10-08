package app.wardrobe.feature.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.Garment
import app.wardrobe.domain.model.ItemColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface EditItemUiState {
    data object Loading : EditItemUiState
    data object NotFound : EditItemUiState

    /** @param hasChanges true while the form differs from the saved item, so leaving asks first. */
    data class Editing(val form: ItemForm, val hasChanges: Boolean) : EditItemUiState
}

/** Edits a copy of one item. The repository changes only when the user saves. */
class EditItemViewModel(
    private val itemId: String,
    private val repository: WardrobeRepository,
    private val defaultName: (Garment, ItemColor) -> String,
) : ViewModel() {

    private var original: ClothingItem? = null
    private val _uiState = MutableStateFlow<EditItemUiState>(EditItemUiState.Loading)
    val uiState: StateFlow<EditItemUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val item = repository.item(itemId).first()
            original = item
            _uiState.value = item?.let { EditItemUiState.Editing(it.toForm(), hasChanges = false) }
                ?: EditItemUiState.NotFound
        }
    }

    fun update(change: ItemFormChange) = editForm { it.apply(change, defaultName) }

    /** Saves the item and returns it, or shows the form's errors and returns null. */
    suspend fun save(): ClothingItem? {
        val form = (_uiState.value as? EditItemUiState.Editing)?.form ?: return null
        if (!form.isValid) {
            editForm { it.copy(showErrors = true) }
            return null
        }
        return form.toItem(itemId).also { repository.update(it) }
    }

    private fun editForm(change: (ItemForm) -> ItemForm) {
        _uiState.update { state ->
            if (state !is EditItemUiState.Editing) return@update state
            val form = change(state.form)
            EditItemUiState.Editing(form, hasChanges = form.toItem(itemId) != original)
        }
    }
}
