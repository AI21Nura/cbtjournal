package com.ainsln.feature.notes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.notes.adaptive.NotesListDetailScreen
import com.ainsln.feature.notes.components.NoteDetailsPlaceholder
import com.ainsln.feature.notes.details.NoteDetailsScreen
import com.ainsln.feature.notes.editor.NoteEditorScreen
import kotlinx.serialization.Serializable

sealed interface NotesDestinations {
    @Serializable
    data object List : NotesDestinations
    @Serializable
    data class Details(val id: Long) : NotesDestinations
    @Serializable
    data class Editor(val id: Long? = null) : NotesDestinations
    @Serializable
    data object DetailPlaceholder : NotesDestinations
}

fun NavGraphBuilder.notesDestination(
    nestedNavController: NavHostController,
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
){
    composable<NotesDestinations.List>{
        NotesListDetailScreen(distortionsSelectionDialog, nestedNavController)
    }
}

fun NavGraphBuilder.noteDetailsDestination(
    onEditClick: (Long) -> Unit,
    onBack: () -> Unit,
    canNavigateBack: Boolean
){
    composable<NotesDestinations.Details>{
        NoteDetailsScreen(onEditClick, onBack, canNavigateBack)
    }
}


fun NavGraphBuilder.noteDetailsPlaceholder(){
    composable<NotesDestinations.DetailPlaceholder>{
        NoteDetailsPlaceholder()
    }
}

fun NavGraphBuilder.noteEditorDestination(
    distortionsSelectionDialog: @Composable (MultiSelectionDialogArgs) -> Unit,
    showWarningDialog: () -> Unit,
    navigateToNoteDetails: (Long) -> Unit,
    onBack: () -> Unit
){
    composable<NotesDestinations.Editor>{
        NoteEditorScreen(
            distortionsSelectionDialog = distortionsSelectionDialog,
            showWarningDialog = showWarningDialog,
            navigateToNoteDetails = navigateToNoteDetails,
            onBack = onBack
        )
    }
}

fun NavController.navigateToJournal(navOptions: NavOptions){
    navigate(route = NotesDestinations.List, navOptions)
}
