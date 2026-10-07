package app.wardrobe.feature.wardrobe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.Category
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.ItemColor
import app.wardrobe.domain.model.Season
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed interface WardrobeUiState {
    data object Loading : WardrobeUiState

    /**
     * @param items the items that match [filter], grouped by category and sorted by name.
     * @param hasAnyItems false when the wardrobe itself is empty, as opposed to the filter matching nothing.
     * @param availableColors the colors offered in the filter sheet: those in the wardrobe, plus any
     *   selected color whose last item was deleted, so the user can still unselect it.
     */
    data class Loaded(
        val items: List<ClothingItem>,
        val filter: WardrobeFilter,
        val hasAnyItems: Boolean,
        val availableColors: List<ItemColor>,
    ) : WardrobeUiState
}

class WardrobeViewModel(repository: WardrobeRepository) : ViewModel() {

    private val filter = MutableStateFlow(WardrobeFilter())

    val uiState: StateFlow<WardrobeUiState> = combine(repository.items, filter) { items, filter ->
        WardrobeUiState.Loaded(
            items = items.filter(filter::matches).sortedWith(compareBy({ it.category }, { it.name })),
            filter = filter,
            hasAnyItems = items.isNotEmpty(),
            availableColors = (items.map { it.color } + filter.colors).distinct().sorted(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), WardrobeUiState.Loading)

    fun selectCategory(category: Category?) {
        filter.update { it.copy(category = category) }
    }

    fun toggleColor(color: ItemColor) {
        filter.update { it.copy(colors = it.colors.toggle(color)) }
    }

    fun toggleSeason(season: Season) {
        filter.update { it.copy(seasons = it.seasons.toggle(season)) }
    }

    fun clearFilters() {
        filter.value = WardrobeFilter()
    }

    private fun <T> Set<T>.toggle(value: T): Set<T> = if (value in this) this - value else this + value
}
