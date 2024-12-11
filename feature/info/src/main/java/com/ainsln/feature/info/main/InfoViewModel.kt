package com.ainsln.feature.info.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.api.InfoRepository
import com.ainsln.core.data.util.ResourceManager
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.utils.IntentSender
import com.ainsln.feature.info.R
import com.ainsln.feature.info.state.InfoUiState
import com.ainsln.feature.info.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InfoViewModel @Inject constructor(
    infoRepository: InfoRepository,
    private val intentSender: IntentSender,
    private val resourceManager: ResourceManager
) : ViewModel() {

    val uiState: StateFlow<InfoUiState> = infoRepository.getInfoContent()
        .map { it.toState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun sendFeedback(context: Context, email:String){
        intentSender.send(
            context = context,
            subject = resourceManager.getString(R.string.feedback_subject),
            text = "",
            email = email
        )
    }

}
