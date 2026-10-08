package app.wardrobe.feature.item

import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.Garment
import app.wardrobe.domain.model.ItemColor
import app.wardrobe.domain.model.ItemSuggestion
import app.wardrobe.domain.model.Season
import app.wardrobe.domain.model.Warmth

/**
 * The editable fields of an item.
 *
 * @param nameEditedByUser false while [name] is the generated default, which then follows garment and color changes.
 * @param suggestion what recognition proposed, shown next to the matching choices.
 * @param showErrors becomes true after a failed save, so errors don't show before the user tries.
 */
data class ItemForm(
    val name: String,
    val nameEditedByUser: Boolean,
    val garment: Garment,
    val color: ItemColor,
    val warmth: Warmth,
    val seasons: Set<Season>,
    val suggestion: ItemSuggestion? = null,
    val showErrors: Boolean = false,
) {
    val nameError: Boolean get() = name.isBlank()
    val seasonsError: Boolean get() = seasons.isEmpty()
    val isValid: Boolean get() = !nameError && !seasonsError

    fun toItem(id: String) = ClothingItem(id, name.trim(), garment, color, warmth, seasons)
}
