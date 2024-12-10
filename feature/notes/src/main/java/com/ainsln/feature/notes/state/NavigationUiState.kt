package com.ainsln.feature.notes.state

data class NavigationUiState(
    val isEditorScreenOpen: Boolean = false,
    val clickedNoteId: Long? = null,
    val currentEditingNote: Long? = null,
    val showWarningDialog: Boolean = false
)
