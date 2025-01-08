package com.ainsln.feature.notes.adaptive

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ainsln.feature.notes.navigation.NotesDestinations

class DetailPaneNavController(navController: NavHostController) {

    var nestedNavController: NavHostController = navController

    @Composable
    fun UpdateController(){
        nestedNavController = rememberNavController()
    }

    fun navigateToNotePlaceholder(){
        navigateToDestination(NotesDestinations.DetailPlaceholder)
    }

    fun navigateToNoteDetails(id: Long){
        navigateToDestination(NotesDestinations.Details(id))
    }

    fun navigateToNoteEditor(id: Long?){
        navigateToDestination(NotesDestinations.Editor(id))
    }

    private fun navigateToDestination(destination: NotesDestinations){
        nestedNavController.navigate(destination){
            popUpTo(NoteDetailPaneNavHost)
        }
    }

}
