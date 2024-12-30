package com.ainsln.feature.notes.state

import com.ainsln.feature.notes.navigation.NotesDestinations


data class NavigationUiState(
    val currentDestination: NotesDestinations = NotesDestinations.DetailPlaceholder,
    val showSearchScreen: Boolean = false,
    val isEditorScreenOpen: Boolean = false,
    val currentNoteId: Long? = null,
    val clickedNoteId: Long? = null,
    val showWarningDialog: Boolean = false
)
