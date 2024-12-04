package com.ainsln.feature.notes.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.notes.list.NotesScreen
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.navigation.navigateToNoteDetails
import com.ainsln.feature.notes.navigation.navigateToNoteEntry
import com.ainsln.feature.notes.navigation.navigateToNotePlaceholder
import com.ainsln.feature.notes.navigation.noteDetailsDestination
import com.ainsln.feature.notes.navigation.noteDetailsPlaceholder
import com.ainsln.feature.notes.navigation.noteEntryDestination
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object NoteDetailPaneNavHost

@Composable
internal fun NotesListDetailScreen(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
) {
    NotesListDetailContent(distortionsSelectionDialog, windowAdaptiveInfo)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun NotesListDetailContent(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    windowAdaptiveInfo: WindowAdaptiveInfo,

) {
    val coroutineScope = rememberCoroutineScope()

    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo),
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.List),
        )
    )

    var nestedNavHostStartDestination: NotesDestinations by remember {
        mutableStateOf(NotesDestinations.DetailPlaceholder)
    }

    val backHandler = {
        coroutineScope.launch {
            listDetailNavigator.navigateBack()
            nestedNavHostStartDestination = NotesDestinations.DetailPlaceholder
        }
    }

    BackHandler(listDetailNavigator.canNavigateBack()) { backHandler() }

    val nestedNavController = rememberNavController()

    fun onDetailPaneClick(
        navigateTo: NavController.() -> Unit,
        destination: NotesDestinations
    ) {
        coroutineScope.launch {
            if (listDetailNavigator.areBothPanesVisible()) {
                nestedNavController.navigateTo()
            } else {
                nestedNavHostStartDestination = destination
            }
            listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    fun onNoteDetailsClick(noteId: Long){
        onDetailPaneClick(destination = NotesDestinations.Detail(noteId), navigateTo = {
            nestedNavController.navigateToNoteDetails(noteId) {
                popUpTo(NoteDetailPaneNavHost)
            }
        })
    }

    fun onNoteEntryClick(id: Long? = null){
        onDetailPaneClick(destination = NotesDestinations.Entry(id), navigateTo = {
            nestedNavController.navigateToNoteEntry(id) {
                popUpTo(NoteDetailPaneNavHost)
            }
        })
    }

    fun onBack() {
        if (listDetailNavigator.areBothPanesVisible()) {
            nestedNavController.navigateToNotePlaceholder {
                popUpTo(NoteDetailPaneNavHost)
            }
        } else
            backHandler()
    }

    ListDetailPaneScaffold(
        directive = listDetailNavigator.scaffoldDirective,
        value = listDetailNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                NotesScreen(
                    onNoteClick = ::onNoteDetailsClick,
                    onAddNoteClick = ::onNoteEntryClick
                )
            }
        },
        detailPane = {
            AnimatedPane {
                NavHost(
                    navController = nestedNavController,
                    startDestination = nestedNavHostStartDestination,
                    route = NoteDetailPaneNavHost::class
                ) {
                    noteDetailsDestination(
                        onEditClick = ::onNoteEntryClick,
                        onBack = ::onBack,
                        canNavigateBack = listDetailNavigator.canNavigateBack()
                    )
                    noteDetailsPlaceholder()
                    noteEntryDestination(
                        distortionsSelectionDialog= distortionsSelectionDialog,
                        navigateToNoteDetails = ::onNoteDetailsClick,
                        onBack = ::onBack
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.areBothPanesVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
            && scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
