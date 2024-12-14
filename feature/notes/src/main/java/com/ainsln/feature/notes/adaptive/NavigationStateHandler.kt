package com.ainsln.feature.notes.adaptive

import com.ainsln.feature.notes.state.NavigationUiState
import kotlinx.coroutines.flow.StateFlow

interface NotesNavigationStateHandler {
    val navigationState: StateFlow<NavigationUiState>

    fun onNoteDetailsClick(id: Long)
    fun onEditorScreenClick(id: Long? = null)
    fun toggleWarningDialog(shown: Boolean)
    fun toggleShowSearch(shown: Boolean)
    fun resetState()
}
