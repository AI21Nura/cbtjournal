package com.ainsln.feature.info.state

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.GuideContent
import com.ainsln.core.model.InfoContent
import com.ainsln.core.ui.state.UiState

fun <T> Result<T>.toState(): UiState<T> {
    return when(this){
        is Result.Loading -> UiState.Loading
        is Result.Error -> UiState.Error(e)
        is Result.Success -> UiState.Success(data)
    }
}

typealias InfoUiState = UiState<InfoContent>
typealias GuideUiState = UiState<GuideContent>
