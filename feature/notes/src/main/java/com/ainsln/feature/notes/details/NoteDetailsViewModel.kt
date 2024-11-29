package com.ainsln.feature.notes.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ainsln.core.data.repository.NotesRepository
import com.ainsln.core.domain.GetFullNoteUseCase
import com.ainsln.core.model.Note
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.state.ActionState
import com.ainsln.feature.notes.state.NoteDetailsUiState
import com.ainsln.feature.notes.state.toActionState
import com.ainsln.feature.notes.state.toState
import com.ainsln.feature.notes.utils.NoteFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getFullNoteUseCase: GetFullNoteUseCase,
    private val notesRepository: NotesRepository,
    private val noteFormatter: NoteFormatter
) : ViewModel() {

    private val noteId = savedStateHandle.toRoute<NotesDestinations.Detail>().id

    val noteState: StateFlow<NoteDetailsUiState> =
        getFullNoteUseCase(noteId).map { it.toState() }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

    val deleteState: MutableStateFlow<ActionState<Unit>> = MutableStateFlow(ActionState.Idle)

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note).collectLatest { result ->
                deleteState.update { result.toActionState() }
            }
        }
    }

    fun resetDeleteState() {
        deleteState.update { ActionState.Idle }
    }

    fun buildTextForSending(note: Note): String {
        return noteFormatter.buildTextForSending(note)
    }
}
