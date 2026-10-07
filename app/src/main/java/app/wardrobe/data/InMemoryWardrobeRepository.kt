package app.wardrobe.data

import app.wardrobe.domain.model.ClothingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/** Keeps items in memory. Changes are lost when the app process ends. */
class InMemoryWardrobeRepository(initialItems: List<ClothingItem>) : WardrobeRepository {

    private val state = MutableStateFlow(initialItems)

    override val items: StateFlow<List<ClothingItem>> = state.asStateFlow()

    override fun item(id: String): Flow<ClothingItem?> =
        state.map { items -> items.firstOrNull { it.id == id } }.distinctUntilChanged()

    override suspend fun add(item: ClothingItem) {
        state.update { items ->
            require(items.none { it.id == item.id }) { "Item ${item.id} already exists" }
            items + item
        }
    }

    override suspend fun update(item: ClothingItem) {
        state.update { items ->
            require(items.any { it.id == item.id }) { "No item ${item.id}" }
            items.map { if (it.id == item.id) item else it }
        }
    }

    override suspend fun delete(id: String) {
        state.update { items -> items.filterNot { it.id == id } }
    }
}
