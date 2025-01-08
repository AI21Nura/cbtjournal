package com.ainsln.feature.notes.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.ShortNote
import com.ainsln.core.ui.components.RenderUiStateScaffold
import com.ainsln.core.ui.components.appbar.SelectionModeTopAppBar
import com.ainsln.core.ui.components.appbar.TopDestinationAppBar
import com.ainsln.core.ui.components.dialog.NoteAlertDialog
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.theme.SelectedItemColor
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.components.EmptyNotesList
import com.ainsln.feature.notes.state.NotesListUiState
import com.ainsln.feature.notes.state.SelectionState
import com.ainsln.feature.notes.utils.formatDate

@Composable
fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    showFAB: Boolean,
    onSearchClick: () -> Unit,
    onDeleteSelected: (List<Long>) -> Unit,
    listState: LazyListState,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val uiState by viewModel.notesState.collectAsStateWithLifecycle()
    val selectionState by viewModel.selectionState.collectAsStateWithLifecycle()

    NotesScreenContent(
        uiState = uiState,
        selectionState = selectionState,
        toggleSelectionMode = viewModel::toggleSelectionMode,
        toggleSelectedElement = viewModel::toggleSelectedElement,
        onNoteClick = onNoteClick,
        onAddNoteClick = onAddNoteClick,
        onDeleteSelectedClick = {
            onDeleteSelected(selectionState.selectedItems)
            viewModel.deleteSelected()
        },
        showFAB = showFAB,
        onSearchClick = onSearchClick,
        listState = listState
    )
}

@Composable
internal fun NotesScreenContent(
    uiState: NotesListUiState,
    selectionState: SelectionState,
    toggleSelectionMode: (Boolean) -> Unit,
    toggleSelectedElement: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit,
    showFAB: Boolean,
    onSearchClick: () -> Unit,
    listState: LazyListState
) {
    var isDeleteDialogOpen by remember { mutableStateOf(false) }

    RenderUiStateScaffold(
        uiState = uiState,
        topBarTitle = R.string.notes_list_title,
        errMsgRes = R.string.notes_list_error,
        onBack = {},
        canNavigateUp = false
    ) { data ->
        NotesScreenScaffold(
            selectionState = selectionState,
            resetSelectionMode = { toggleSelectionMode(false) },
            onSearchClick = onSearchClick,
            onAddNoteClick = onAddNoteClick,
            onDeleteSelectedClick = { isDeleteDialogOpen = true },
            showFAB = showFAB
        ) { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)) {
                MultiSelectionNotesList(
                    notes = data,
                    selectionState = selectionState,
                    toggleSelectionMode = toggleSelectionMode,
                    toggleSelectedElement = toggleSelectedElement,
                    onNoteClick = onNoteClick,
                    listState = listState
                )

                if (isDeleteDialogOpen) {
                    NoteAlertDialog(
                        title = stringResource(R.string.delete_notes),
                        text = stringResource(R.string.delete_selected_warning),
                        onDismissClick = { isDeleteDialogOpen = false },
                        onConfirmClick = {
                            onDeleteSelectedClick()
                            isDeleteDialogOpen = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotesScreenScaffold(
    selectionState: SelectionState,
    resetSelectionMode: () -> Unit,
    onSearchClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onDeleteSelectedClick: () -> Unit,
    showFAB: Boolean,
    content: @Composable (PaddingValues) -> Unit
){
    if (selectionState.isSelectionMode){
        Scaffold(
            topBar = {
                SelectionModeTopAppBar(
                    selectedItemsNumber = selectionState.selectedItems.size,
                    resetSelectionMode = resetSelectionMode,
                    onDeleteClick = onDeleteSelectedClick
                )
            }
        ){ content(it) }
    } else {
        Scaffold(
            topBar = {
                TopDestinationAppBar(
                    title = stringResource(R.string.notes_list_title),
                    alignCenter = true
                ){
                    Row {
                        IconButton(onClick = onSearchClick) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(R.string.search)
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                if (showFAB){
                    FloatingActionButton(onClick = onAddNoteClick) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.add_note)
                        )
                    }
                }
            }
        ){ content(it) }
    }
}

@Composable
internal fun MultiSelectionNotesList(
    notes: List<ShortNote>,
    selectionState: SelectionState,
    toggleSelectionMode: (Boolean) -> Unit,
    toggleSelectedElement: (Long) -> Unit,
    onNoteClick: (Long) -> Unit,
    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    modifier: Modifier = Modifier
) {
    BackHandler(selectionState.isSelectionMode) { toggleSelectionMode(false) }

    if (notes.isNotEmpty())
        LazyColumn(
            contentPadding = contentPadding,
            state = listState,
            modifier = modifier
        ) {
            items(notes) { note ->
                NoteListItem(
                    note = note,
                    onNoteClick = { id ->
                        if (selectionState.isSelectionMode) toggleSelectedElement(id)
                        else onNoteClick(id)
                    },
                    onNoteLongClick = { id ->
                        if (!selectionState.isSelectionMode) {
                            toggleSelectionMode(true)
                            toggleSelectedElement(id)
                        }
                    },
                    isChecked = if (selectionState.isSelectionMode) selectionState.selectedItems.contains(
                        note.id
                    )
                    else null
                )
            }
        }
    else
        EmptyNotesList()
}


@Composable
internal fun NotesList(
    notes: List<ShortNote>,
    onNoteClick: (Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        items(notes) { note ->
            NoteListItem(note, onNoteClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NoteListItem(
    note: ShortNote,
    onNoteClick: (Long) -> Unit,
    isChecked: Boolean? = null,
    onNoteLongClick: ((Long) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val cardColors =
        if (isChecked != null && isChecked)
            CardDefaults.cardColors(containerColor = SelectedItemColor)
        else
            CardDefaults.outlinedCardColors()

    OutlinedCard(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = cardColors,
        modifier = modifier
            .combinedClickable(
                onClick = { onNoteClick(note.id) },
                onLongClick = { onNoteLongClick?.invoke(note.id) }
            )
            .padding(bottom = 12.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = note.situation,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(formatDate(note.date))
                    EmotionsList(note.emotions)
                }
            },
            leadingContent = {
                isChecked?.let {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { }
                    )
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent),
            modifier = Modifier
                .padding(vertical = 4.dp)
                .testTag("note:${note.id}")
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EmotionsList(emotions: List<SelectedEmotion>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        emotions.forEach { emotion ->
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = Color(emotion.emotion.color)
                ),
                border = BorderStroke(0.5.dp, Color.Black),
            ) {
                Text(
                    text = emotion.emotion.name,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NotesScreenSelectedModePreview() {
    CBTJournalTheme {
        NotesScreenContent(
            uiState = UiState.Success(NotesPreviewData.shortNotes),
            selectionState = SelectionState(selectedItems = mutableListOf(1), isSelectionMode = true),
            toggleSelectionMode = {},
            toggleSelectedElement = {},
            onNoteClick = { },
            onAddNoteClick = {},
            onDeleteSelectedClick = {},
            showFAB = true,
            onSearchClick = {},
            listState = rememberLazyListState()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    CBTJournalTheme {
        NotesScreenContent(
            uiState = UiState.Success(NotesPreviewData.shortNotes),
            selectionState = SelectionState(),
            toggleSelectionMode = {},
            toggleSelectedElement = {},
            onNoteClick = { },
            onAddNoteClick = {},
            onDeleteSelectedClick = {},
            showFAB = true,
            onSearchClick = {},
            listState = rememberLazyListState()
        )
    }
}
