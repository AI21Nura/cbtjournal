package com.ainsln.feature.notes.state

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Note
import com.ainsln.core.model.RecentSearch
import com.ainsln.core.model.ShortNote
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.components.circle.Circle

fun <T> Result<T>.toState(): UiState<T> {
    return when(this){
        is Result.Loading -> UiState.Loading
        is Result.Error -> UiState.Error(e)
        is Result.Success -> UiState.Success(data)
    }
}

typealias NoteDetailsUiState = UiState<Note>
typealias NotesListUiState = UiState<List<ShortNote>>
typealias EmotionsDialogUiState = UiState<Circle>
typealias RecentSearchUiState = UiState<List<RecentSearch>>
