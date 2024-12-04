package com.ainsln.feature.notes.state

import com.ainsln.core.data.result.Result
import com.ainsln.feature.notes.components.circle.Circle

sealed interface EmotionsDialogUiState {
    data object Loading : EmotionsDialogUiState
    data class Success(val data: Circle) : EmotionsDialogUiState
    data class Error(val e: Throwable) : EmotionsDialogUiState
}

fun <T: Any> Result<T>.toUiState(): EmotionsDialogUiState{
    return when(this){
        is Result.Loading -> EmotionsDialogUiState.Loading
        is Result.Error -> EmotionsDialogUiState.Error(e)
        is Result.Success -> EmotionsDialogUiState.Success(Circle.DEFAULT)
    }
}
