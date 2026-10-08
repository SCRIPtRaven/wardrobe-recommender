package app.wardrobe.feature.item

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.wardrobe.R
import app.wardrobe.ui.components.BottomActionBar
import app.wardrobe.domain.model.ClothingItem
import app.wardrobe.ui.components.EmptyState
import kotlinx.coroutines.launch

@Composable
fun EditItemScreen(
    viewModel: EditItemViewModel,
    onClose: () -> Unit,
    onSaved: (ClothingItem) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    EditItemContent(
        state = state,
        onClose = onClose,
        onFormChange = viewModel::update,
        onSave = { scope.launch { viewModel.save()?.let(onSaved) } },
    )
}

/** Stateless edit item screen. [showDiscardDialogInitially] lets screenshot tests open the dialog. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemContent(
    state: EditItemUiState,
    onClose: () -> Unit,
    onFormChange: (ItemFormChange) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    showDiscardDialogInitially: Boolean = false,
) {
    var showDiscardDialog by rememberSaveable { mutableStateOf(showDiscardDialogInitially) }
    val hasChanges = (state as? EditItemUiState.Editing)?.hasChanges == true
    BackHandler(enabled = hasChanges) { showDiscardDialog = true }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_item_title)) },
                navigationIcon = {
                    IconButton(onClick = { if (hasChanges) showDiscardDialog = true else onClose() }) {
                        Icon(painterResource(R.drawable.ic_close), stringResource(R.string.action_close))
                    }
                },
            )
        },
        bottomBar = {
            if (state is EditItemUiState.Editing) BottomActionBar(stringResource(R.string.edit_item_save), onSave)
        },
    ) { padding ->
        when (state) {
            EditItemUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            EditItemUiState.NotFound -> EmptyState(
                icon = R.drawable.ic_checkroom,
                title = stringResource(R.string.item_not_found_title),
                body = stringResource(R.string.item_not_found_body),
                modifier = Modifier.padding(padding),
                actionLabel = stringResource(R.string.action_back),
                onAction = onClose,
            )

            is EditItemUiState.Editing -> ItemFormFields(
                form = state.form,
                onChange = onFormChange,
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }

    if (showDiscardDialog) {
        DiscardDialog(
            title = stringResource(R.string.edit_item_discard_title),
            body = stringResource(R.string.edit_item_discard_body),
            onDiscard = {
                showDiscardDialog = false
                onClose()
            },
            onKeepEditing = { showDiscardDialog = false },
        )
    }
}
