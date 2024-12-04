package com.ainsln.feature.notes.entry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ainsln.core.data.repository.api.DistortionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.domain.GetFullNoteUseCase
import com.ainsln.core.domain.GetSelectedEmotionsUseCase
import com.ainsln.core.domain.SaveFullNoteUseCase
import com.ainsln.core.model.Thought
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.state.NoteDetails
import com.ainsln.feature.notes.state.NoteEntryUiState
import com.ainsln.feature.notes.state.toActionState
import com.ainsln.feature.notes.state.toNote
import com.ainsln.feature.notes.state.toNoteDetails
import com.ainsln.feature.notes.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class NoteEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFullNoteUseCase: GetFullNoteUseCase,
    private val distortionsRepository: DistortionsRepository,
    private val getSelectedEmotionsUseCase: GetSelectedEmotionsUseCase,
    private val saveFullNoteUseCase: SaveFullNoteUseCase
) : ViewModel(), NoteModifier {

    private val noteId = savedStateHandle.toRoute<NotesDestinations.Entry>().id

    private val _uiState: MutableStateFlow<NoteEntryUiState> = MutableStateFlow(NoteEntryUiState())
    val uiState: StateFlow<NoteEntryUiState> = _uiState

    init {
        noteId?.let { loadNote(it) }
    }

    override fun updateDate(millis: Long) {
        _uiState.update { oldState ->
            oldState.copy(noteDetails = oldState.noteDetails.copy(date = Date(millis)))
        }
    }

    override fun updateTextField(noteDetails: NoteDetails) {
        _uiState.update { oldState ->
            oldState.copy(noteDetails = noteDetails)
        }
    }

    override fun toggleDistortionsDialog(isOpen: Boolean) {
        _uiState.update { oldState ->
            oldState.copy(isDistortionsDialogOpen = isOpen)
        }
    }

    override fun updateDistortionsList(ids: List<Long>) {
        viewModelScope.launch {
            distortionsRepository.getDistortionsByIds(ids)
                .collectLatest { result ->
                    if (result !is Result.Success) return@collectLatest
                    _uiState.update { oldState ->
                        oldState.copy(
                            noteDetails = oldState.noteDetails.copy(
                                distortions = result.data
                            )
                        )
                    }
                }
        }
    }

    override fun removeDistortion(id: Long) {
        val oldList = _uiState.value.noteDetails.distortions
        _uiState.update { oldState ->
            oldState.copy(noteDetails = oldState.noteDetails.copy(
                distortions = oldList.filter { it.id != id }
            ))
        }
    }

    override fun addThought() {
        _uiState.value.noteDetails.thoughts.add(
            Thought(id = 0, text = "", alternativeThought = "")
        )
    }

    override fun removeThought(index: Int) {
        _uiState.value.noteDetails.thoughts.removeAt(index)
    }

    override fun updateThoughtText(index: Int, text: String) {
        _uiState.value.noteDetails.thoughts[index] =
            _uiState.value.noteDetails.thoughts[index].copy(text = text)
    }

    override fun updateAlternativeThought(index: Int, alternative: String) {
        _uiState.value.noteDetails.thoughts[index] =
            _uiState.value.noteDetails.thoughts[index].copy(alternativeThought = alternative)
    }

    override fun toggleEmotionsDialog(isOpen: Boolean) {
        _uiState.update { oldState ->
            oldState.copy(isEmotionsDialogOpen = isOpen)
        }
    }

    override fun updateEmotionIntensityBefore(index: Int, intensity: Int) {
        _uiState.value.noteDetails.emotions[index] =
            _uiState.value.noteDetails.emotions[index].copy(intensityBefore = intensity)
    }

    override fun removeEmotion(index: Int) {
        _uiState.value.noteDetails.emotions.removeAt(index)
    }

    override fun updateEmotionsList(ids: List<Long>) {
        _uiState.value.noteDetails.emotions.removeIf { !ids.contains(it.emotion.id) }
        viewModelScope.launch {
            val oldIds = _uiState.value.noteDetails.emotions.map { it.emotion.id }
            val newIds = ids.filter { !oldIds.contains(it) }

            getSelectedEmotionsUseCase(newIds).collectLatest { result ->
                if (result is Result.Success) {
                    val emotions = _uiState.value.noteDetails.emotions.apply { addAll(result.data) }
                    _uiState.update { oldState ->
                        oldState.copy(noteDetails = oldState.noteDetails.copy(emotions = emotions))
                    }
                }
            }
        }
    }

    override fun updateEmotionIntensityAfter(index: Int, intensity: Int) {
        _uiState.value.noteDetails.emotions[index] =
            _uiState.value.noteDetails.emotions[index].copy(intensityAfter = intensity)
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { oldState ->
            oldState.copy(currentTabIndex = tabIndex)
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            if (_uiState.value.loadingState !is UiState.Success || !validateData()) return@launch
            val oldNote = (_uiState.value.loadingState as UiState.Success).data

            saveFullNoteUseCase(_uiState.value.noteDetails.toNote(oldNote?.id), oldNote)
                .collectLatest { result ->
                    _uiState.update { oldState ->
                        oldState.copy(saveState = result.toActionState())
                    }
                }
            delay(5000)

        }
    }

    private fun validateData(): Boolean {
        val validationResult = _uiState.value.noteDetails.checkRequiredFields()
        _uiState.update { oldState ->
            oldState.copy(missingFields = validationResult)
        }
        return validationResult.isEmpty()
    }

    private fun loadNote(id: Long){
        viewModelScope.launch {
            getFullNoteUseCase(id).map { it.toState() }.collectLatest { state ->
                when(state){
                    is UiState.Success -> {
                        _uiState.update { it.copy(
                            loadingState = UiState.Success(state.data),
                            noteDetails = state.data.toNoteDetails(),
                            isEditingMode = true
                        ) }
                        cancel()
                    }
                    else -> _uiState.update { it.copy(loadingState = state) }
                }
            }
        }
    }


}
