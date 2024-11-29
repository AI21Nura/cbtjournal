package com.ainsln.feature.notes.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.domain.GetAllNotesUseCase
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.state.NotesListUiState
import com.ainsln.feature.notes.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    getAllNotesUseCase: GetAllNotesUseCase
) : ViewModel() {

    val uiState: StateFlow<NotesListUiState> = getAllNotesUseCase("en").map { it.toState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

}
