package com.ainsln.feature.notes.editor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.ui.components.RenderUiStateScaffold
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.components.dialog.NoteAlertDialog
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.editor.dialog.DistortionsDialog
import com.ainsln.feature.notes.editor.dialog.EmotionsDialog
import com.ainsln.feature.notes.editor.tabs.EditorScreenTab
import com.ainsln.feature.notes.editor.tabs.InterpretationTab
import com.ainsln.feature.notes.editor.tabs.ReframingTab
import com.ainsln.feature.notes.editor.tabs.SituationTab
import com.ainsln.feature.notes.state.ActionState
import com.ainsln.feature.notes.state.NoteEditorUiState

@Composable
fun NoteEditorScreen(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    showWarningDialog: () -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: NoteEditorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoteEditorContent(
        uiState = uiState,
        showWarningDialog = showWarningDialog,
        fieldsModifier = viewModel,
        onSelectTab = viewModel::selectTab,
        distortionsSelectionDialog = distortionsSelectionDialog,
        navigateToNoteDetails = navigateToNoteDetails,
        onSaveClick = viewModel::saveNote,
        onBack = onBack
    )
}

@Composable
fun NoteEditorContent(
    uiState: NoteEditorUiState,
    showWarningDialog: () -> Unit,
    fieldsModifier: NoteModifier,
    onSelectTab: (Int) -> Unit,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
) {
    RenderUiStateScaffold(
        uiState = uiState.loadingState,
        topBarTitle = R.string.note_edit_title,
        errMsgRes = R.string.notes_details_error,
        onBack = onBack,
        canNavigateUp = true
    ) {
        NoteEditor(
            uiState = uiState,
            showWarningDialog = showWarningDialog,
            fieldsModifier = fieldsModifier,
            onSelectTab = onSelectTab,
            distortionsSelectionDialog = distortionsSelectionDialog,
            navigateToNoteDetails = navigateToNoteDetails,
            onSaveClick = onSaveClick,
        )
    }
}

@Composable
fun NoteEditor(
    uiState: NoteEditorUiState,
    showWarningDialog: () -> Unit,
    fieldsModifier: NoteModifier,
    onSelectTab: (Int) -> Unit,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onSaveClick: () -> Unit,
) {
    var isValidationDialogOpen by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMsg = stringResource(R.string.cant_save_note)

    BackHandler { showWarningDialog() }

    LaunchedEffect(uiState.saveState) {
        if (uiState.saveState is ActionState.Error){
            snackbarHostState.showSnackbar(message = snackbarMsg)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            DetailsAppBar(
                title = stringResource(
                    if (uiState.isEditingMode) R.string.note_edit_title
                    else R.string.note_add_title
                ),
                onBack = { showWarningDialog() }
            ) {
                when(uiState.saveState){
                    is ActionState.Idle, is ActionState.Error -> {
                        IconButton(onClick = {
                            isValidationDialogOpen = true
                            onSaveClick()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = stringResource(R.string.save_note)
                            )
                        }
                    }
                    is ActionState.Loading -> {
                        CircularProgressIndicator(Modifier.padding(24.dp).size(32.dp))
                    }
                    is ActionState.Success -> {
                        navigateToNoteDetails(uiState.saveState.data)
                    }
                }
            }
        }
    ) { innerPadding ->
        NoteEditorTabs(
            uiState = uiState,
            onSelectTab = onSelectTab,
            fieldsModifier = fieldsModifier,
            contentPadding = innerPadding
        )

        if (uiState.isDistortionsDialogOpen) {
            DistortionsDialog(
                initListIds = uiState.noteDetails.distortions.map { it.id },
                toggleDistortionsDialog = fieldsModifier::toggleDistortionsDialog,
                onUpdateDistortionsList = fieldsModifier::updateDistortionsList,
                distortionsSelectionDialog = distortionsSelectionDialog
            )
        }

        if (uiState.isEmotionsDialogOpen){
            EmotionsDialog(
                initListIds = uiState.noteDetails.emotions.map { it.emotion.id },
                toggleEmotionsDialog = fieldsModifier::toggleEmotionsDialog,
                onUpdateEmotionsList = fieldsModifier::updateEmotionsList
            )
        }

        if (isValidationDialogOpen && uiState.missingFields.isNotBlank()){
            NoteAlertDialog(
                title = stringResource(R.string.validation_error),
                text = stringResource(R.string.validation_warning, uiState.missingFields),
                onDismissClick = { isValidationDialogOpen = false },
                onConfirmClick = { isValidationDialogOpen = false },
                hasCancelButton = false
            )
        }
    }
}

@Composable
fun NoteEditorTabs(
    uiState: NoteEditorUiState,
    onSelectTab: (Int) -> Unit,
    fieldsModifier: NoteModifier,
    contentPadding: PaddingValues,
){
    val pagerState = rememberPagerState { EditorScreenTab.entries.size }
    LaunchedEffect(uiState.currentTabIndex) {
        pagerState.scrollToPage(uiState.currentTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        onSelectTab(pagerState.currentPage)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
    ) {
        ScrollableTabRow(
            selectedTabIndex = uiState.currentTabIndex
        ) {
            EditorScreenTab.entries.forEach { tab ->
                Tab(
                    selected = tab.index == uiState.currentTabIndex,
                    onClick = { onSelectTab(tab.index) },
                    text = { Text(stringResource(tab.titleResId)) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) { pageIndex ->
            when (pageIndex) {
                EditorScreenTab.Situation.index -> {
                    SituationTab(
                        uiState = uiState,
                        fieldsModifier = fieldsModifier
                    )
                }
                EditorScreenTab.Interpretation.index -> {
                    InterpretationTab(
                        uiState = uiState,
                        fieldsModifier = fieldsModifier
                    )
                }
                EditorScreenTab.Reframing.index -> {
                    ReframingTab(
                        uiState = uiState,
                        fieldsModifier = fieldsModifier
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoteEditorScreenPreview() {
    CBTJournalTheme {
        NoteEditor(
            uiState = NoteEditorUiState(),
            showWarningDialog = {},
            fieldsModifier = NotesPreviewData.fieldsModifier,
            onSelectTab = {},
            distortionsSelectionDialog = {},
            navigateToNoteDetails = {},
            onSaveClick = {},
        )
    }
}
