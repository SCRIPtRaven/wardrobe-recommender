package app.wardrobe.feature.wardrobe

import app.wardrobe.domain.model.Category
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.domain.model.ItemColor
import app.wardrobe.domain.model.Season

/**
 * What the wardrobe grid shows. Null [category] means every category.
 * An empty [colors] or [seasons] set means no restriction on that attribute.
 */
data class WardrobeFilter(
    val category: Category? = null,
    val colors: Set<ItemColor> = emptySet(),
    val seasons: Set<Season> = emptySet(),
) {
    /** True when the filter sheet restricts colors or seasons. The category chips are outside the sheet. */
    val hasSheetFilters: Boolean get() = colors.isNotEmpty() || seasons.isNotEmpty()

    val isActive: Boolean get() = category != null || hasSheetFilters

    fun matches(item: ClothingItem): Boolean =
        (category == null || item.category == category) &&
            (colors.isEmpty() || item.color in colors) &&
            (seasons.isEmpty() || item.seasons.any { it in seasons })
}
