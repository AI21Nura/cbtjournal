package com.ainsln.feature.notes.adaptive

import androidx.navigation.NavHostController
import com.ainsln.feature.notes.navigation.NotesDestinations

class DetailPaneNavController(val controller: NavHostController) {

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
        controller.navigate(destination){
            popUpTo(NoteDetailPaneNavHost)
        }
    }

}
