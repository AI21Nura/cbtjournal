package com.ainsln.feature.notes.entry

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.components.dialog.NoteAlertDialog
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.entry.dialog.DistortionsDialog
import com.ainsln.feature.notes.entry.dialog.EmotionsDialog
import com.ainsln.feature.notes.entry.tabs.EntryScreenTab
import com.ainsln.feature.notes.entry.tabs.SituationTab
import com.ainsln.feature.notes.entry.tabs.InterpretationTab
import com.ainsln.feature.notes.entry.tabs.ReframingTab
import com.ainsln.feature.notes.state.NoteEntryUiState
import com.ainsln.feature.notes.state.ActionState

@Composable
fun NoteEntryScreen(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: NoteEntryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoteEntryContent(
        uiState = uiState,
        fieldsModifier = viewModel,
        onSelectTab = viewModel::selectTab,
        distortionsSelectionDialog = distortionsSelectionDialog,
        navigateToNoteDetails = navigateToNoteDetails,
        onSaveClick = viewModel::saveNote,
        onBack = onBack
    )
}

@Composable
fun NoteEntryContent(
    uiState: NoteEntryUiState,
    fieldsModifier: NoteModifier,
    onSelectTab: (Int) -> Unit,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
) {
    when (uiState.loadingState) {
        is UiState.Success -> {
            NoteEntry(
                uiState = uiState,
                fieldsModifier = fieldsModifier,
                onSelectTab = onSelectTab,
                distortionsSelectionDialog = distortionsSelectionDialog,
                navigateToNoteDetails = navigateToNoteDetails,
                onSaveClick = onSaveClick,
                onBack = onBack,
            )
        }
        is UiState.Loading -> { LoadingScreen() }
        is UiState.Error -> {
            ErrorScreen(message = "Error loading note\n" + uiState.loadingState.e.message)
        }
    }
}

@Composable
fun NoteEntry(
    uiState: NoteEntryUiState,
    fieldsModifier: NoteModifier,
    onSelectTab: (Int) -> Unit,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
) {
    var isCancellationDialogOpen by remember { mutableStateOf(false) }
    var isValidationDialogOpen by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler { isCancellationDialogOpen = true }

    LaunchedEffect(uiState.saveState) {
        if (uiState.saveState is ActionState.Error){
            snackbarHostState.showSnackbar(message = "Can't save note")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            DetailsAppBar(
                title = "Note",
                onBack = { isCancellationDialogOpen = true }
            ) {
                when(uiState.saveState){
                    is ActionState.Idle, is ActionState.Error -> {
                        IconButton(onClick = {
                            isValidationDialogOpen = true
                            onSaveClick()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Save"
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
        NoteEntryTabs(
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

        if (isCancellationDialogOpen){
            NoteAlertDialog(
                title = "Discard Changes",
                text = "All unsaved changes will be lost. Are you sure you want to exit?",
                onDismissClick = { isCancellationDialogOpen = false },
                onConfirmClick = {
                    isCancellationDialogOpen = false
                    if (uiState.isEditingMode)
                        navigateToNoteDetails(uiState.noteDetails.id)
                    else
                        onBack()
                }
            )
        }

        if (isValidationDialogOpen && uiState.missingFields.isNotBlank()){
            NoteAlertDialog(
                title = "Validation Error",
                text = "Please fill in the required fields:\n" + uiState.missingFields,
                onDismissClick = { isValidationDialogOpen = false },
                onConfirmClick = { isValidationDialogOpen = false },
                hasCancelButton = false
            )
        }
    }
}

@Composable
fun NoteEntryTabs(
    uiState: NoteEntryUiState,
    onSelectTab: (Int) -> Unit,
    fieldsModifier: NoteModifier,
    contentPadding: PaddingValues,
){
    val pagerState = rememberPagerState { EntryScreenTab.entries.size }
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
                start = 12.dp,
                end = 12.dp
            )
    ) {

        ScrollableTabRow(
            selectedTabIndex = uiState.currentTabIndex
        ) {
            EntryScreenTab.entries.forEach { tab ->
                Tab(
                    selected = tab.index == uiState.currentTabIndex,
                    onClick = { onSelectTab(tab.index) },
                    text = { Text(tab.title) }
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
                EntryScreenTab.Situation.index -> {
                    SituationTab(
                        uiState = uiState,
                        fieldsModifier = fieldsModifier
                    )
                }
                EntryScreenTab.Interpretation.index -> {
                    InterpretationTab(
                        uiState = uiState,
                        fieldsModifier = fieldsModifier
                    )
                }
                EntryScreenTab.Reframing.index -> {
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
fun NoteEntryScreenPreview() {
    CBTJournalTheme {
        NoteEntry(
            uiState = NoteEntryUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            onSelectTab = {},
            distortionsSelectionDialog = {},
            navigateToNoteDetails = {},
            onSaveClick = {},
            onBack = {}
        )
    }
}
