package com.ainsln.feature.notes.state

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.ShortNote

sealed interface SearchResultUiState {

    data object EmptyResult : SearchResultUiState

    data object Loading : SearchResultUiState

    data class LoadFailed(val e: Throwable) : SearchResultUiState

    data class Success(val notes: List<ShortNote>) : SearchResultUiState
}

fun Result<List<ShortNote>>.toSearchState(): SearchResultUiState {
    return when(this){
        is Result.Loading -> SearchResultUiState.Loading
        is Result.Error -> SearchResultUiState.LoadFailed(e)
        is Result.Success -> {
            if (data.isEmpty()) SearchResultUiState.EmptyResult
            else SearchResultUiState.Success(data)
        }
    }
}
