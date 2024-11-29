package com.ainsln.core.ui.state

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val e: Throwable) : UiState<Nothing>
}
