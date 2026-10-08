package app.wardrobe.feature.item

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.wardrobe.R
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.ui.components.GarmentBackdrop
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel,
    onClose: () -> Unit,
    onSaved: (ClothingItem) -> Unit,
) {
    val step by viewModel.step.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    AddItemContent(
        step = step,
        onClose = onClose,
        onChooseSource = viewModel::recognizePhoto,
        onFormChange = viewModel::update,
        onSave = { scope.launch { viewModel.save()?.let(onSaved) } },
    )
}

/** Stateless add item screen. [showDiscardDialogInitially] lets screenshot tests open the dialog. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemContent(
    step: AddItemStep,
    onClose: () -> Unit,
    onChooseSource: () -> Unit,
    onFormChange: (ItemFormChange) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    showDiscardDialogInitially: Boolean = false,
) {
    var showDiscardDialog by rememberSaveable { mutableStateOf(showDiscardDialogInitially) }
    val hasInput = step is AddItemStep.Details
    BackHandler(enabled = hasInput) { showDiscardDialog = true }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_item_title)) },
                navigationIcon = {
                    IconButton(onClick = { if (hasInput) showDiscardDialog = true else onClose() }) {
                        Icon(painterResource(R.drawable.ic_close), stringResource(R.string.action_close))
                    }
                },
            )
        },
        bottomBar = {
            if (step is AddItemStep.Details) {
                Surface(color = MaterialTheme.colorScheme.surfaceContainer) {
                    Button(
                        onClick = onSave,
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                    ) {
                        Text(stringResource(R.string.add_item_save))
                    }
                }
            }
        },
    ) { padding ->
        when (step) {
            AddItemStep.ChooseSource -> SourceChoice(onChooseSource, Modifier.padding(padding))
            AddItemStep.Analyzing -> Analyzing(Modifier.padding(padding))
            is AddItemStep.Details -> ItemFormFields(
                form = step.form,
                onChange = onFormChange,
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }

    if (showDiscardDialog) {
        AlertDialog(
            onDismissRequest = { showDiscardDialog = false },
            title = { Text(stringResource(R.string.add_item_discard_title)) },
            text = { Text(stringResource(R.string.add_item_discard_body)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDiscardDialog = false
                        onClose()
                    },
                ) {
                    Text(stringResource(R.string.action_discard))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDiscardDialog = false }) {
                    Text(stringResource(R.string.add_item_keep_editing))
                }
            },
        )
    }
}

@Composable
private fun SourceChoice(onChoose: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        SourceCard(
            icon = R.drawable.ic_photo_camera,
            title = stringResource(R.string.add_item_camera),
            body = stringResource(R.string.add_item_camera_body),
            onClick = onChoose,
        )
        SourceCard(
            icon = R.drawable.ic_photo_library,
            title = stringResource(R.string.add_item_gallery),
            body = stringResource(R.string.add_item_gallery_body),
            onClick = onChoose,
        )
        Row(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_lightbulb),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = stringResource(R.string.add_item_tip),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SourceCard(@DrawableRes icon: Int, title: String, body: String, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun Analyzing(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(GarmentBackdrop),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_auto_awesome),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.tertiary,
            )
        }
        Text(stringResource(R.string.add_item_analyzing), style = MaterialTheme.typography.titleMedium)
        LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(
            text = stringResource(R.string.add_item_analyzing_body),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
