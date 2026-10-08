package app.wardrobe.feature.outfits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.DayForecast
import app.wardrobe.domain.model.Forecast
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed interface OutfitGeneratorUiState {
    data object Loading : OutfitGeneratorUiState

    /**
     * @param items the wardrobe, grouped by category, to pick the item to build outfits around.
     * @param selectedItem the chosen item, or null if none is chosen or it was deleted.
     */
    data class Ready(
        val items: List<ClothingItem>,
        val selectedItem: ClothingItem?,
        val forecast: Forecast,
        val selectedDay: DayForecast,
    ) : OutfitGeneratorUiState {
        val canShowOutfits: Boolean get() = selectedItem != null
    }
}

/** The Outfits tab: choose an item and a day of the [forecast]. Starts on the forecast's first day. */
class OutfitGeneratorViewModel(
    repository: WardrobeRepository,
    private val forecast: Forecast,
) : ViewModel() {

    private val selectedItemId = MutableStateFlow<String?>(null)
    private val selectedDate = MutableStateFlow(forecast.days.first().date)

    val uiState: StateFlow<OutfitGeneratorUiState> =
        combine(repository.items, selectedItemId, selectedDate) { items, itemId, date ->
            OutfitGeneratorUiState.Ready(
                items = items.sortedWith(compareBy({ it.category }, { it.name })),
                selectedItem = items.firstOrNull { it.id == itemId },
                forecast = forecast,
                selectedDay = forecast.days.first { it.date == date },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OutfitGeneratorUiState.Loading)

    fun selectItem(itemId: String) {
        selectedItemId.value = itemId
    }

    fun selectDay(date: LocalDate) {
        require(forecast.days.any { it.date == date }) { "$date is outside the forecast" }
        selectedDate.value = date
    }
}
