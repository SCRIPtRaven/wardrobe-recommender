package app.wardrobe.feature.item

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.wardrobe.R

// Pieces shared by the add and edit item screens.

/** Full-width save button pinned to the bottom of a form screen. */
@Composable
fun SaveBar(label: String, onSave: () -> Unit) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
        Button(
            onClick = onSave,
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Text(label)
        }
    }
}

/** Asks before leaving a form with unsaved input. */
@Composable
fun DiscardDialog(title: String, body: String, onDiscard: () -> Unit, onKeepEditing: () -> Unit) {
    AlertDialog(
        onDismissRequest = onKeepEditing,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = {
            TextButton(onClick = onDiscard) { Text(stringResource(R.string.action_discard)) }
        },
        dismissButton = {
            TextButton(onClick = onKeepEditing) { Text(stringResource(R.string.add_item_keep_editing)) }
        },
    )
}
