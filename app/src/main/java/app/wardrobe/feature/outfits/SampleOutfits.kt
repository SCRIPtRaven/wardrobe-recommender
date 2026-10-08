package app.wardrobe.feature.outfits

import androidx.annotation.StringRes
import app.wardrobe.R

/** A hand-written outfit for the milestone 1 demo. [itemIds] refer to the sample wardrobe. */
data class SampleOutfit(
    val id: String,
    @StringRes val title: Int,
    val itemIds: List<String>,
    @StringRes val reasons: List<Int>,
)

/**
 * The outfits the milestone 1 demo shows, all built around [ANCHOR_ITEM_ID].
 * The recommendation engine in milestone 2 replaces them with generated outfits for any item.
 */
object SampleOutfits {
    const val ANCHOR_ITEM_ID = "dark-jeans"

    val all: List<SampleOutfit> = listOf(
        SampleOutfit(
            id = "smart-casual",
            title = R.string.outfit_smart_casual,
            itemIds = listOf("camel-coat", "oxford-shirt", ANCHOR_ITEM_ID, "brown-boots"),
            reasons = listOf(R.string.reason_neutral_base, R.string.reason_single_accent, R.string.reason_coat_for_cold),
        ),
        SampleOutfit(
            id = "rainy-day",
            title = R.string.outfit_rainy_day,
            itemIds = listOf("olive-rain-jacket", "cream-knit", ANCHOR_ITEM_ID, "brown-boots"),
            reasons = listOf(R.string.reason_rain_ready, R.string.reason_earth_tones, R.string.reason_knit_warmth),
        ),
        SampleOutfit(
            id = "easy-weekend",
            title = R.string.outfit_easy_weekend,
            itemIds = listOf("burgundy-sweater", ANCHOR_ITEM_ID, "white-sneakers"),
            reasons = listOf(R.string.reason_accent_on_denim, R.string.reason_light_shoes, R.string.reason_mild_day),
        ),
    )
}
