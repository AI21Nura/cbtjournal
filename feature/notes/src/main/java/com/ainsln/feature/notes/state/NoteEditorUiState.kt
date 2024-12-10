package com.ainsln.feature.notes.state

import com.ainsln.core.model.Note
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.editor.tabs.EditorScreenTab

data class NoteEditorUiState(
    val loadingState: UiState<Note?> = UiState.Success(null),
    val noteDetails: NoteDetails = NoteDetails(),
    val isEditingMode: Boolean = false,
    val isDistortionsDialogOpen: Boolean = false,
    val isEmotionsDialogOpen: Boolean = false,
    val currentTabIndex: Int = EditorScreenTab.Situation.index,
    val saveState: ActionState<Long> = ActionState.Idle,
    val missingFields: String = ""
)




