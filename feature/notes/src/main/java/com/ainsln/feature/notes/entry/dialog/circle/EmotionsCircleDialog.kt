package com.ainsln.feature.notes.entry.dialog.circle

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.components.dialog.BasicDialog
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.notes.components.circle.ClickableCircle
import com.ainsln.feature.notes.state.EmotionsDialogUiState
import kotlin.math.min

@Composable
fun EmotionsCircleDialog(
    args: MultiSelectionDialogArgs,
){
    val viewModel: EmotionsCircleViewModel = hiltViewModel()

    val density = LocalDensity.current
    var screenWidthDp by remember{ mutableStateOf(0.dp) }

    val circleSizePx = with(density) {
        screenWidthDp.toPx()
    }

    viewModel.updateCircleSize(circleSizePx)
    viewModel.setInitSelectedList(args.initListIds)

    val uiState by viewModel.circleUiState.collectAsStateWithLifecycle()

    BasicDialog(
        title = "Select distortions",
        onSaveClick = {
            args.callbacks.onSave(viewModel.getSelectedList())
        },
        onCloseClick = args.callbacks::onClose,
        modifier = Modifier.onGloballyPositioned {
            screenWidthDp = with(density){
                min((it.size.width*0.8).toInt(), (it.size.height*0.8).toInt()).toDp()
            }
        }
    ) {
        if (screenWidthDp > 0.dp){
            EmotionsCircleDialogContent(
                uiState = uiState,
                circleSize = screenWidthDp,
                onAddEmotion = viewModel::addSelection,
                onRemoveEmotion = viewModel::removeSelection
            )
        }
    }
}

@Composable
fun EmotionsCircleDialogContent(
    uiState: EmotionsDialogUiState,
    circleSize: Dp,
    onAddEmotion: (Long) -> Unit,
    onRemoveEmotion: (Long) -> Unit
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        when(uiState){
            is EmotionsDialogUiState.Loading -> LoadingScreen()
            is EmotionsDialogUiState.Error -> ErrorScreen(uiState.e.message ?: "Some problems with emotions")
            is EmotionsDialogUiState.Success -> {
                Box(modifier = Modifier.size(circleSize).focusable(false)) {
                    ClickableCircle(
                        uiState.data,
                        onAddEmotion,
                        onRemoveEmotion
                    )
                }
            }
        }
    }
}
