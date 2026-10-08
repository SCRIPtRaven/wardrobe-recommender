package app.wardrobe.feature.outfits

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.wardrobe.data.WardrobeRepository
import app.wardrobe.domain.model.Category
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.DayForecast
import app.wardrobe.domain.model.Forecast
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class Rating { LIKED, DISLIKED }

/** An outfit as the results screen shows it, with [items] in display order. */
data class OutfitCard(
    val id: String,
    @StringRes val title: Int,
    val items: List<ClothingItem>,
    @StringRes val reasons: List<Int>,
    val rating: Rating?,
)

sealed interface OutfitResultsUiState {
    data object Loading : OutfitResultsUiState
    data object NotFound : OutfitResultsUiState

    /** @param outfits empty when there are no outfits for [anchor]. */
    data class Ready(
        val anchor: ClothingItem,
        val location: String,
        val today: LocalDate,
        val day: DayForecast,
        val outfits: List<OutfitCard>,
    ) : OutfitResultsUiState
}

/**
 * Outfits built around one item for one forecast day.
 *
 * INFO(limit): milestone 1 shows [SampleOutfits] for the sample anchor and nothing for other items.
 * Ratings live only as long as this screen. The recommendation engine and feedback storage replace both.
 */
class OutfitResultsViewModel(
    itemId: String,
    date: LocalDate,
    repository: WardrobeRepository,
    forecast: Forecast,
) : ViewModel() {

    private val ratings = MutableStateFlow<Map<String, Rating>>(emptyMap())
    private val day = forecast.days.firstOrNull { it.date == date } ?: forecast.days.first()

    val uiState: StateFlow<OutfitResultsUiState> =
        combine(repository.items, ratings) { items, ratings ->
            val anchor = items.firstOrNull { it.id == itemId } ?: return@combine OutfitResultsUiState.NotFound
            OutfitResultsUiState.Ready(
                anchor = anchor,
                location = forecast.location,
                today = forecast.days.first().date,
                day = day,
                outfits = if (anchor.id == SampleOutfits.ANCHOR_ITEM_ID) outfitCards(items, ratings) else emptyList(),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), OutfitResultsUiState.Loading)

    /** Sets [rating] on the outfit, or clears it if the outfit already has that rating. */
    fun rate(outfitId: String, rating: Rating) {
        ratings.update { current ->
            if (current[outfitId] == rating) current - outfitId else current + (outfitId to rating)
        }
    }

    private fun outfitCards(items: List<ClothingItem>, ratings: Map<String, Rating>): List<OutfitCard> {
        val byId = items.associateBy { it.id }
        return SampleOutfits.all.mapNotNull { outfit ->
            // An outfit drops out when the user has deleted one of its items.
            val outfitItems = outfit.itemIds.map { byId[it] ?: return@mapNotNull null }
            OutfitCard(
                id = outfit.id,
                title = outfit.title,
                items = outfitItems.sortedBy { DISPLAY_ORDER.indexOf(it.category) },
                reasons = outfit.reasons,
                rating = ratings[outfit.id],
            )
        }
    }

    private companion object {
        val DISPLAY_ORDER = listOf(Category.OUTERWEAR, Category.TOP, Category.BOTTOM, Category.FOOTWEAR)
    }
}
