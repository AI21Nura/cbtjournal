package com.ainsln.feature.notes.adaptive

import androidx.lifecycle.ViewModel
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.state.NavigationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NotesAdaptiveViewModel @Inject constructor()
    : ViewModel(), NotesNavigationStateHandler {

    private val _navigationState: MutableStateFlow<NavigationUiState> = MutableStateFlow(NavigationUiState())
    override val navigationState: StateFlow<NavigationUiState> = _navigationState

    override fun onNoteDetailsClick(id: Long) {
        _navigationState.update { oldState ->
            oldState.copy(
                clickedNoteId = id,
                showWarningDialog = oldState.isEditorScreenOpen
            )
        }
    }

    override fun saveCurrentNoteId(id: Long) {
        _navigationState.update { oldState ->
            oldState.copy(currentNoteId = id)
        }
    }

    override fun onEditorScreenClick(id: Long?) {
        _navigationState.update { oldState ->
            oldState.copy(isEditorScreenOpen = true, currentNoteId = id)
        }
    }

    override fun saveCurrentDestination(destination: NotesDestinations) {
        _navigationState.update { oldState ->
            oldState.copy(currentDestination = destination)
        }
    }

    override fun toggleWarningDialog(shown: Boolean) {
        _navigationState.update { oldState ->
            if (shown)
                oldState.copy(showWarningDialog = true)
            else
                oldState.copy(showWarningDialog = false, clickedNoteId = null)
        }
    }

    override fun toggleShowSearch(shown: Boolean) {
        _navigationState.update { oldState ->
            oldState.copy(showSearchScreen = shown)
        }
    }

    override fun resetState() {
        _navigationState.update {
            NavigationUiState(showSearchScreen = it.showSearchScreen)
        }
    }

}
