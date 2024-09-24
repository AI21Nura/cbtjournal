package com.ainsln.feature.distortions.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.ainsln.core.data.repository.DistortionsRepository
import com.ainsln.feature.distortions.navigation.DistortionsDestinations
import com.ainsln.feature.distortions.state.DistortionDetailUiState
import com.ainsln.feature.distortions.state.DistortionUiState
import com.ainsln.feature.distortions.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DistortionsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    distortionsRepository: DistortionsRepository
) : ViewModel() {

    private val distortionId = savedStateHandle.toRoute<DistortionsDestinations.Detail>().id

    val distortion: StateFlow<DistortionDetailUiState> =
        distortionsRepository.getDistortionById(distortionId)
            .map { it.toState() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                DistortionUiState.Loading
            )


}


