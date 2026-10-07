package app.wardrobe.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object WardrobeRoute : NavKey

@Serializable
data object OutfitsRoute : NavKey

@Serializable
data object SettingsRoute : NavKey

@Serializable
data class ItemDetailRoute(val itemId: String) : NavKey

@Serializable
data object AddItemRoute : NavKey

@Serializable
data class EditItemRoute(val itemId: String) : NavKey

/** Bottom bar destinations in display order. Each one keeps its own back stack. */
val TopLevelRoutes: List<NavKey> = listOf(WardrobeRoute, OutfitsRoute, SettingsRoute)
