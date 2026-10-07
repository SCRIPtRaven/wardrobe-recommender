package app.wardrobe.data

import app.wardrobe.domain.model.ClothingItem
import kotlinx.coroutines.flow.Flow

/** The user's clothing catalog. Milestone 1 keeps it in memory, milestone 2 stores it in Room. */
interface WardrobeRepository {
    val items: Flow<List<ClothingItem>>

    /** Emits the item with [id], or null when there is none. */
    fun item(id: String): Flow<ClothingItem?>

    /** @throws IllegalArgumentException if an item with the same id exists. */
    suspend fun add(item: ClothingItem)

    /** @throws IllegalArgumentException if no item has this item's id. */
    suspend fun update(item: ClothingItem)

    suspend fun delete(id: String)
}
