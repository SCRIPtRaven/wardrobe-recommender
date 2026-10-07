package app.wardrobe.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.wardrobe.domain.model.ItemColor

/** A small circle filled with [color]. The outline keeps white and black visible on any background. */
@Composable
fun ColorSwatch(color: ItemColor, modifier: Modifier = Modifier, size: Dp = 12.dp) {
    Box(
        modifier
            .size(size)
            .background(Color(color.argb), CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
    )
}
