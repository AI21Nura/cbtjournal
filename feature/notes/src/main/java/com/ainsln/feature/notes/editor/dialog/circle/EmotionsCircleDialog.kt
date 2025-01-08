package com.ainsln.feature.notes.editor.dialog.circle

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.ui.components.RenderUiState
import com.ainsln.core.ui.components.dialog.BasicDialog
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.components.circle.ClickableCircle
import com.ainsln.feature.notes.state.EmotionsDialogUiState
import kotlin.math.min

@Composable
fun EmotionsCircleDialog(
    args: MultiSelectionDialogArgs,
) {
    val viewModel: EmotionsCircleViewModel = hiltViewModel()

    val configuration = LocalConfiguration.current
    val screenWidthDp = Dp(min(configuration.screenHeightDp, configuration.screenWidthDp) * 0.8f)
    val circleSizeDp = Dp(screenWidthDp.value * 0.8f)
    val circleSizePx = with(LocalDensity.current) {
        screenWidthDp.toPx() * 0.8f
    }

    viewModel.updateCircleSize(circleSizePx)
    viewModel.setInitSelectedList(args.initListIds)

    val uiState by viewModel.circleUiState.collectAsStateWithLifecycle()

    BasicDialog(
        title = stringResource(R.string.select_distortions),
        onSaveClick = {
            args.callbacks.onSave(viewModel.getSelectedList())
        },
        onCloseClick = args.callbacks::onClose
    ) {
        if (screenWidthDp > 0.dp) {
            EmotionsCircleDialogContent(
                uiState = uiState,
                circleSize = circleSizeDp,
                dialogSize = screenWidthDp,
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
    dialogSize: Dp,
    onAddEmotion: (Long) -> Unit,
    onRemoveEmotion: (Long) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(dialogSize)
    ){
        RenderUiState(
            uiState = uiState,
            errMsgRes = R.string.emotions_error
        ) { data ->
            Box(Modifier.size(circleSize).focusable(false)) {
                ClickableCircle(data, onAddEmotion, onRemoveEmotion)
            }
        }
    }


}
