package com.ainsln.feature.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.domain.GetAllNotesUseCase
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.state.NotesListUiState
import com.ainsln.feature.notes.state.SelectionState
import com.ainsln.feature.notes.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    getAllNotesUseCase: GetAllNotesUseCase,
    private val notesRepository: NotesRepository
) : ViewModel() {

    val notesState: StateFlow<NotesListUiState> = getAllNotesUseCase().map { it.toState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

    private val _selectionState = MutableStateFlow(SelectionState())
    val selectionState: StateFlow<SelectionState> = _selectionState
    private val _selectedItems get() = _selectionState.value.selectedItems

    fun toggleSelectionMode(inMode: Boolean){
        _selectionState.update { SelectionState(isSelectionMode = inMode) }
    }

    fun toggleSelectedElement(id: Long) {
        if (_selectedItems.contains(id)) {
            _selectedItems.remove(id)
            if (_selectedItems.isEmpty())
                toggleSelectionMode(false)
        } else {
            _selectedItems.add(id)
        }
    }

    fun deleteSelected(){
        viewModelScope.launch { notesRepository.deleteNotesById(_selectedItems) }
        toggleSelectionMode(false)
    }

}
