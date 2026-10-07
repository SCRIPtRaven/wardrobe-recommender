package app.wardrobe.navigation

import androidx.navigation3.runtime.NavKey

/** Turns navigation events into changes of [NavigationState]. */
class Navigator(private val state: NavigationState) {

    /** Switches to a tab when [route] is a top-level route, otherwise opens [route] in the selected tab. */
    fun navigate(route: NavKey) {
        val tabStack = state.backStacks[route]
        if (tabStack == null) {
            state.backStacks.getValue(state.topLevelRoute).add(route)
            return
        }
        if (route == state.topLevelRoute) {
            // Selecting the tab that is already selected returns to its root.
            while (tabStack.size > 1) tabStack.removeAt(tabStack.lastIndex)
        }
        state.topLevelRoute = route
    }

    /** Pops the selected tab, or returns to the start tab from another tab's root. */
    fun goBack() {
        val stack = state.backStacks.getValue(state.topLevelRoute)
        if (stack.last() == state.topLevelRoute) {
            state.topLevelRoute = state.startRoute
        } else {
            stack.removeAt(stack.lastIndex)
        }
    }
}
