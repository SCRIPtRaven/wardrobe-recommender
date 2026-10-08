package app.wardrobe.feature.outfits

import app.wardrobe.data.SampleData
import app.wardrobe.domain.model.Category
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SampleOutfitsTest {

    private val items = SampleData.items.associateBy { it.id }

    @Test
    fun thereAreThreeOutfitsWithUniqueIds() {
        assertEquals(3, SampleOutfits.all.size)
        assertEquals(3, SampleOutfits.all.map { it.id }.toSet().size)
    }

    @Test
    fun everyItemIsInTheSampleWardrobe() {
        val missing = SampleOutfits.all.flatMap { it.itemIds }.filterNot { it in items }
        assertTrue("missing $missing", missing.isEmpty())
    }

    @Test
    fun everyOutfitContainsTheAnchor() {
        assertTrue(SampleOutfits.all.all { SampleOutfits.ANCHOR_ITEM_ID in it.itemIds })
    }

    @Test
    fun everyOutfitHasOneTopBottomAndPairOfShoesAndAtMostOneOuterLayer() {
        for (outfit in SampleOutfits.all) {
            val counts = outfit.itemIds.map { items.getValue(it).category }.groupingBy { it }.eachCount()
            assertEquals(outfit.id, 1, counts[Category.TOP])
            assertEquals(outfit.id, 1, counts[Category.BOTTOM])
            assertEquals(outfit.id, 1, counts[Category.FOOTWEAR])
            assertTrue(outfit.id, (counts[Category.OUTERWEAR] ?: 0) <= 1)
        }
    }

    @Test
    fun everyOutfitExplainsItselfWithAtLeastTwoReasons() {
        assertTrue(SampleOutfits.all.all { it.reasons.size >= 2 })
    }
}
