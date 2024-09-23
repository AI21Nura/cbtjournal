package com.ainsln.feature.distortions.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.ainsln.feature.distortions.navigation.DistortionsDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DistortionsDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val id: StateFlow<Long?> = MutableStateFlow(savedStateHandle.toRoute<DistortionsDestinations.Detail>().id)

}
