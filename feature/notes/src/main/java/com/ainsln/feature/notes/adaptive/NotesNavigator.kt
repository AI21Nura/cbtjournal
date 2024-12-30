package com.ainsln.feature.notes.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.navigation.NavHostController
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.state.NavigationUiState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
interface NotesNavigator {
    val scaffoldDirective: PaneScaffoldDirective
    val scaffoldValue: ThreePaneScaffoldValue
    val canNavigateBack: Boolean
    val showFAB: Boolean
    val navigationState: StateFlow<NavigationUiState>

    fun onBack()
    fun handleOpenNoteDeletion(ids: List<Long>)
    fun onNoteDetailsClick(id: Long)
    fun onEditorScreenClick(noteId: Long? = null)
    fun navigateToNoteDetails(id: Long)
    fun toggleWarningDialog(shown: Boolean)
    fun toggleShowSearch(shown: Boolean)
    fun onConfirmCancellation()

    fun getWideScreenNavController(): NavHostController
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class BaseNotesNavigator(
    private val paneNavigator: PaneNavigator,
    private val nestedController: DetailPaneNavController,
    private val stateHandler: NotesNavigationStateHandler
) : NotesNavigator {

    private val _navigationState: NavigationUiState
        get() = stateHandler.navigationState.value

    override val navigationState: StateFlow<NavigationUiState>
        get() = stateHandler.navigationState

    override val canNavigateBack: Boolean
        get() = paneNavigator.canNavigateBack

    override val showFAB: Boolean
        get() = !_navigationState.isEditorScreenOpen

    override val scaffoldDirective get() = paneNavigator.scaffoldDirective
    override val scaffoldValue get() = paneNavigator.scaffoldValue

    override fun onBack() {
        stateHandler.resetState()
        paneNavigator.navigateBack()
        if (paneNavigator.areBothPanesVisible()) {
            nestedController.navigateToNotePlaceholder()
        }
    }
    
    override fun handleOpenNoteDeletion(ids: List<Long>) {
        if (_navigationState.currentNoteId in ids)
            onBack()
    }

    override fun onNoteDetailsClick(id: Long) {
        if (_navigationState.isEditorScreenOpen) {
            stateHandler.onNoteDetailsClick(id)
        } else {
            stateHandler.saveCurrentNoteId(id)
            onDetailPaneClick(
                destination = NotesDestinations.Details(id),
                navigateTo = { nestedController.navigateToNoteDetails(id) }
            )
        }
    }

    override fun onEditorScreenClick(noteId: Long?) {
        stateHandler.onEditorScreenClick(noteId)
        onDetailPaneClick(
            destination = NotesDestinations.Editor(noteId),
            navigateTo = { nestedController.navigateToNoteEditor(noteId) }
        )
    }

    override fun navigateToNoteDetails(id: Long) {
        stateHandler.resetState()
        onNoteDetailsClick(id)
    }

    override fun toggleWarningDialog(shown: Boolean) {
        stateHandler.toggleWarningDialog(shown)
    }

    override fun toggleShowSearch(shown: Boolean) {
        stateHandler.toggleShowSearch(shown)
    }

    override fun getWideScreenNavController(): NavHostController {
        return nestedController.controller
        //return if (paneNavigator.areBothPanesVisible()) nestedController.controller else null
    }

    override fun onConfirmCancellation() {
        val navigationState = _navigationState
        stateHandler.resetState()
        when {
            navigationState.clickedNoteId != null -> onNoteDetailsClick(navigationState.clickedNoteId)

            navigationState.currentNoteId != null -> onNoteDetailsClick(navigationState.currentNoteId)

            else -> onBack()
        }
    }

    private fun onDetailPaneClick(
        navigateTo: () -> Unit,
        destination: NotesDestinations
    ) {
        if (paneNavigator.areBothPanesVisible())
            navigateTo()
        else
            stateHandler.saveCurrentDestination(destination)

        paneNavigator.navigateToDetailsPane()
    }
}
