package com.ainsln.feature.notes.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ainsln.core.ui.components.dialog.NoteAlertDialog
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.list.NotesScreen
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.navigation.noteDetailsDestination
import com.ainsln.feature.notes.navigation.noteDetailsPlaceholder
import com.ainsln.feature.notes.navigation.noteEditorDestination
import com.ainsln.feature.notes.search.NotesSearchScreen
import kotlinx.serialization.Serializable

@Serializable
internal data object NoteDetailPaneNavHost


@Composable
internal fun NotesListDetailScreen(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    controller: NavHostController = rememberNavController(),
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    viewModel: NotesAdaptiveViewModel = hiltViewModel(),
) {
    val nestedNavController = DetailPaneNavController(controller)

    NotesListDetailContent(
        distortionsSelectionDialog,
        rememberNotesNavigator(nestedNavController, viewModel, windowAdaptiveInfo),
        updateNavController = { nestedNavController.UpdateController() }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun NotesListDetailContent(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    notesNavigator: NotesNavigator,
    updateNavController: @Composable () -> Unit
) {
    BackHandler(notesNavigator.canNavigateBack) { notesNavigator.onBack() }
    val navState by notesNavigator.navigationState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    ListDetailPaneScaffold(
        directive = notesNavigator.scaffoldDirective,
        value = notesNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                if (navState.showSearchScreen){
                    NotesSearchScreen(
                        onNoteClick = notesNavigator::onNoteDetailsClick,
                        onBack = { notesNavigator.toggleShowSearch(false) }
                    )
                } else {
                    NotesScreen(
                        onNoteClick = notesNavigator::onNoteDetailsClick,
                        onAddNoteClick = notesNavigator::onEditorScreenClick,
                        showFAB = notesNavigator.showFAB,
                        onSearchClick = { notesNavigator.toggleShowSearch(true) },
                        onDeleteSelected = notesNavigator::handleOpenNoteDeletion,
                        listState = listState
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                updateNavController()
                DetailNavHost(
                    startDestination = navState.currentDestination,
                    notesNavigator = notesNavigator,
                    distortionsSelectionDialog = distortionsSelectionDialog
                )
            }

        }
    )
    WarningDialog(navState.showWarningDialog, notesNavigator)
}

@Composable
private fun DetailNavHost(
    startDestination: NotesDestinations,
    notesNavigator: NotesNavigator,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit
) {
    NavHost(
        navController = notesNavigator.getWideScreenNavController(),
        startDestination = startDestination,
        route = NoteDetailPaneNavHost::class
    ) {
        noteDetailsDestination(
            onEditClick = notesNavigator::onEditorScreenClick,
            onBack = notesNavigator::onBack,
            canNavigateBack = notesNavigator.canNavigateBack
        )
        noteDetailsPlaceholder()
        noteEditorDestination(
            distortionsSelectionDialog = distortionsSelectionDialog,
            navigateToNoteDetails = notesNavigator::navigateToNoteDetails,
            showWarningDialog = { notesNavigator.toggleWarningDialog(true) },
            onBack = notesNavigator::onBack
        )
    }
}

@Composable
fun WarningDialog(
    showWarningDialog: Boolean,
    notesNavigator: NotesNavigator
){
    if (showWarningDialog){
        NoteAlertDialog(
            title = stringResource(R.string.discard_changes),
            text = stringResource(R.string.discard_changes_warning),
            onDismissClick = { notesNavigator.toggleWarningDialog(false) },
            onConfirmClick = notesNavigator::onConfirmCancellation
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun rememberNotesNavigator(
    nestedNavController: DetailPaneNavController,
    stateHandler: NotesNavigationStateHandler,
    windowAdaptiveInfo: WindowAdaptiveInfo
) : NotesNavigator {
    val coroutineScope = rememberCoroutineScope()
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo),
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.List),
        )
    )

    val baseNavigator = remember {
        BaseNotesNavigator(
            paneNavigator = PaneNavigator(coroutineScope, listDetailNavigator),
            nestedController = nestedNavController,
            stateHandler = stateHandler
        )
    }

    return baseNavigator
}
