package com.ainsln.feature.distortions.state

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Distortion
import com.ainsln.core.ui.state.UiState

fun <T : Any> Result<T>.toState(): UiState<T> {
    return when (this) {
        is Result.Loading -> UiState.Loading
        is Result.Error -> UiState.Error(e)
        is Result.Success -> UiState.Success(data)
    }
}

typealias DistortionsListUiState = UiState<List<Distortion>>
typealias DistortionDetailUiState = UiState<Distortion>
