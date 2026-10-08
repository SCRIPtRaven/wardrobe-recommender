package app.wardrobe.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import app.wardrobe.domain.model.ClothingItem

/** Provided by the app's navigation. Null in previews and screenshot tests, where nothing animates. */
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

/** The enter and exit animation of the current navigation entry. Null outside navigation. */
val LocalEntryAnimatedScope = compositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * An item's image on the garment backdrop. The image travels between the screens that show it,
 * such as from a grid card to the detail screen, when navigation provides the animation scopes.
 */
@Composable
fun ItemImage(
    item: ClothingItem,
    shape: Shape,
    imagePadding: Dp,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    val sharedScope = LocalSharedTransitionScope.current
    val animatedScope = LocalEntryAnimatedScope.current
    val shared = if (sharedScope != null && animatedScope != null) {
        with(sharedScope) {
            Modifier.sharedElement(
                sharedContentState = rememberSharedContentState(key = "item-image-${item.id}"),
                animatedVisibilityScope = animatedScope,
            )
        }
    } else {
        Modifier
    }
    Box(
        modifier
            .then(shared)
            .clip(shape)
            .background(GarmentBackdrop),
    ) {
        GarmentImage(
            garment = item.garment,
            color = item.color,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(imagePadding),
        )
    }
}
