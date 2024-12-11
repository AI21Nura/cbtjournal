package com.ainsln.feature.info.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.api.InfoRepository
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.info.state.GuideUiState
import com.ainsln.feature.info.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
    infoRepository: InfoRepository
) : ViewModel() {

    val uiState: StateFlow<GuideUiState> = infoRepository.getGuide()
        .map { it.toState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
    )

}
