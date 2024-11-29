package com.ainsln.feature.distortions.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.DistortionsRepository
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.distortions.state.DistortionsListUiState
import com.ainsln.feature.distortions.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DistortionsViewModel @Inject constructor(
    distortionsRepository: DistortionsRepository
) : ViewModel() {

    val uiState: StateFlow<DistortionsListUiState> = distortionsRepository.getDistortions()
        .map { result -> result.toState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )
}

