package app.wardrobe.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import app.wardrobe.R

/** The app's root: the bottom bar and the screen of the selected tab. */
@Composable
fun WardrobeApp() {
    val navigationState = rememberNavigationState(startRoute = WardrobeRoute, topLevelRoutes = TopLevelRoutes)
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val entryProvider = entryProvider<NavKey> {
        entry<WardrobeRoute> { TabPlaceholder(R.string.tab_wardrobe) }
        entry<OutfitsRoute> { TabPlaceholder(R.string.tab_outfits) }
        entry<SettingsRoute> { TabPlaceholder(R.string.tab_settings) }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = navigationState.isAtTabRoot,
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                WardrobeNavigationBar(
                    selected = navigationState.topLevelRoute,
                    onSelect = navigator::navigate,
                )
            }
        },
        // Each screen handles the system bar insets that the bottom bar doesn't cover.
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        NavDisplay(
            entries = navigationState.toEntries(entryProvider),
            onBack = navigator::goBack,
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding),
        )
    }
}

private enum class Tab(
    val route: NavKey,
    @StringRes val label: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
) {
    WARDROBE(WardrobeRoute, R.string.tab_wardrobe, R.drawable.ic_checkroom, R.drawable.ic_checkroom),
    OUTFITS(OutfitsRoute, R.string.tab_outfits, R.drawable.ic_auto_awesome, R.drawable.ic_auto_awesome_filled),
    SETTINGS(SettingsRoute, R.string.tab_settings, R.drawable.ic_settings, R.drawable.ic_settings_filled),
}

@Composable
private fun WardrobeNavigationBar(selected: NavKey, onSelect: (NavKey) -> Unit) {
    NavigationBar {
        Tab.entries.forEach { tab ->
            val isSelected = tab.route == selected
            NavigationBarItem(
                selected = isSelected,
                onClick = { onSelect(tab.route) },
                icon = {
                    Icon(
                        painter = painterResource(if (isSelected) tab.selectedIcon else tab.icon),
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(tab.label)) },
            )
        }
    }
}

// TODO: replace with the Outfits and Settings screens in plan steps 10 and 12.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TabPlaceholder(@StringRes title: Int) {
    Scaffold(topBar = { TopAppBar(title = { Text(stringResource(title)) }) }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
        )
    }
}
