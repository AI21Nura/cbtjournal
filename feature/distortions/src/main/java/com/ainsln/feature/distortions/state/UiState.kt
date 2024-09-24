package com.ainsln.feature.distortions.state

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Distortion

sealed interface DistortionUiState<out T> {
    data object Loading : DistortionUiState<Nothing>
    data class Success<T>(val data: T) : DistortionUiState<T>
    data class Error(val e: Throwable) : DistortionUiState<Nothing>
}

fun <T : Any> Result<T>.toState(): DistortionUiState<T> {
    return when (this) {
        is Result.Loading -> DistortionUiState.Loading
        is Result.Error -> DistortionUiState.Error(e)
        is Result.Success -> DistortionUiState.Success(data)
    }
}

typealias DistortionsListUiState = DistortionUiState<List<Distortion>>
typealias DistortionDetailUiState = DistortionUiState<Distortion>
