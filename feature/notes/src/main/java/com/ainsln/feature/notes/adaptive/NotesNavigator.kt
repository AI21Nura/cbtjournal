package com.ainsln.feature.notes.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.navigation.navigateToNoteDetails
import com.ainsln.feature.notes.navigation.navigateToNoteEditor
import com.ainsln.feature.notes.navigation.navigateToNotePlaceholder
import com.ainsln.feature.notes.state.NavigationUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
interface NotesNavigator {
    val scaffoldDirective: PaneScaffoldDirective
    val scaffoldValue: ThreePaneScaffoldValue
    val canNavigateBack: Boolean
    val showFAB: Boolean
    val currentDestination: NotesDestinations
    val showWarningDialog: StateFlow<Boolean>

    fun backHandler()
    fun onBack()
    fun onNoteDetailsClick(id: Long)
    fun onEditorScreenClick(noteId: Long? = null)
    fun navigateToNoteDetails(id: Long)
    fun toggleWarningDialog(shown: Boolean)
    fun onConfirmCancellation()

    fun getNestedNavController(): NavHostController
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class BaseNotesNavigator(
    private val coroutineScope: CoroutineScope,
    private val listDetailNavigator: ThreePaneScaffoldNavigator<Nothing>,
    private val nestedNavController: NavHostController,
    private val stateHandler: NotesNavigationStateHandler
) : NotesNavigator {

    private val _navigationState: NavigationUiState
        get() = stateHandler.navigationState.value

    override val canNavigateBack: Boolean
        get() = listDetailNavigator.canNavigateBack()

    override val showFAB: Boolean
        get() = !_navigationState.isEditorScreenOpen

    override val currentDestination: NotesDestinations
        get() = nestedNavHostStartDestination

    override val showWarningDialog: StateFlow<Boolean> =
        stateHandler.navigationState.map { it.showWarningDialog }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = false
            )

    override val scaffoldDirective = listDetailNavigator.scaffoldDirective
    override val scaffoldValue = listDetailNavigator.scaffoldValue

    private var nestedNavHostStartDestination: NotesDestinations by mutableStateOf(NotesDestinations.DetailPlaceholder)

    override fun backHandler() {
        coroutineScope.launch {
            listDetailNavigator.navigateBack()
            nestedNavHostStartDestination = NotesDestinations.DetailPlaceholder
        }
    }

    override fun onBack() {
        stateHandler.resetState()
        if (listDetailNavigator.areBothPanesVisible()) {
            nestedNavController.navigateToNotePlaceholder {
                popUpTo(NoteDetailPaneNavHost)
            }
        } else
            backHandler()
    }

    override fun onNoteDetailsClick(id: Long) {
        if (_navigationState.isEditorScreenOpen) {
            stateHandler.onNoteDetailsClick(id)
        } else {
            onDetailPaneClick(
                destination = NotesDestinations.Detail(id),
                navigateTo = {
                    nestedNavController.navigateToNoteDetails(id) {
                        popUpTo(NoteDetailPaneNavHost)
                    }
                }
            )
        }
    }

    override fun onEditorScreenClick(noteId: Long?) {
        stateHandler.onEditorScreenClick(noteId)
        onDetailPaneClick(
            destination = NotesDestinations.Editor(noteId),
            navigateTo = {
                nestedNavController.navigateToNoteEditor(noteId) {
                    popUpTo(NoteDetailPaneNavHost)
                }
            }
        )
    }

    override fun navigateToNoteDetails(id: Long) {
        stateHandler.resetState()
        onNoteDetailsClick(id)
    }

    override fun toggleWarningDialog(shown: Boolean) {
        stateHandler.toggleWarningDialog(shown)
    }

    override fun getNestedNavController() = nestedNavController

    override fun onConfirmCancellation() {
        val navigationState = _navigationState
        stateHandler.resetState()
        when {
            navigationState.currentEditingNote != null -> onNoteDetailsClick(navigationState.currentEditingNote)
            navigationState.clickedNoteId != null -> onNoteDetailsClick(navigationState.clickedNoteId)
            else -> onBack()
        }
    }

    private fun onDetailPaneClick(
        navigateTo: NavController.() -> Unit,
        destination: NotesDestinations
    ) {
        coroutineScope.launch {
            if (listDetailNavigator.areBothPanesVisible())
                nestedNavController.navigateTo()
            else
                nestedNavHostStartDestination = destination

            listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    private fun <T> ThreePaneScaffoldNavigator<T>.areBothPanesVisible(): Boolean =
        scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
                && scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
}
