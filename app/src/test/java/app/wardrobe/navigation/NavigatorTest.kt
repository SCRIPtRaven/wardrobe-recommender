package app.wardrobe.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigatorTest {

    private val state = NavigationState(
        startRoute = WardrobeRoute,
        topLevelRoute = mutableStateOf(WardrobeRoute),
        backStacks = TopLevelRoutes.associateWith { NavBackStack<NavKey>(it) },
    )
    private val navigator = Navigator(state)

    @Test
    fun navigatePushesOntoCurrentTab() {
        navigator.navigate(ItemDetailRoute("shirt"))
        assertEquals(listOf(WardrobeRoute, ItemDetailRoute("shirt")), state.backStacks.getValue(WardrobeRoute).toList())
        assertEquals(ItemDetailRoute("shirt"), state.currentRoute)
    }

    @Test
    fun navigateToAnotherTabKeepsTheFirstTabsHistory() {
        navigator.navigate(ItemDetailRoute("shirt"))
        navigator.navigate(OutfitsRoute)
        assertEquals(OutfitsRoute, state.topLevelRoute)
        assertEquals(OutfitsRoute, state.currentRoute)

        navigator.navigate(WardrobeRoute)
        assertEquals(ItemDetailRoute("shirt"), state.currentRoute)
    }

    @Test
    fun navigateToTheCurrentTabReturnsToItsRoot() {
        navigator.navigate(ItemDetailRoute("shirt"))
        navigator.navigate(EditItemRoute("shirt"))
        navigator.navigate(WardrobeRoute)
        assertEquals(listOf<NavKey>(WardrobeRoute), state.backStacks.getValue(WardrobeRoute).toList())
    }

    @Test
    fun goBackPopsWithinTheCurrentTab() {
        navigator.navigate(ItemDetailRoute("shirt"))
        navigator.goBack()
        assertEquals(WardrobeRoute, state.currentRoute)
    }

    @Test
    fun goBackFromAnotherTabRootReturnsToTheStartTab() {
        navigator.navigate(SettingsRoute)
        navigator.goBack()
        assertEquals(WardrobeRoute, state.topLevelRoute)
    }

    @Test
    fun bottomBarShowsOnlyOnTabRoots() {
        assertTrue(state.isAtTabRoot)
        navigator.navigate(AddItemRoute)
        assertFalse(state.isAtTabRoot)
    }
}
