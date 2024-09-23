package com.ainsln.feature.distortions.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.DistortionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Distortion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DistortionsViewModel @Inject constructor(
    private val distortionsRepository: DistortionsRepository
) : ViewModel() {

    val uiState: StateFlow<DistortionsUiState> = distortionsRepository.getDistortions()
        .map { result -> result.toState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DistortionsUiState.Loading
        )

}

sealed interface DistortionsUiState {
    data object Loading : DistortionsUiState
    data class Error(val e: Throwable) : DistortionsUiState
    data class Success(val distortions: List<Distortion>) : DistortionsUiState
}

fun Result<List<Distortion>>.toState(): DistortionsUiState{
    return when(this){
        is Result.Loading -> DistortionsUiState.Loading
        is Result.Error -> DistortionsUiState.Error(e)
        is Result.Success -> DistortionsUiState.Success(data)
    }
}

