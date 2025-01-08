package com.ainsln.core.ui.components.dialog

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun AppDialog(
    title: String,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit
){
    if (windowAdaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT) {
        FullScreenDialog(
            title,
            onSaveClick,
            onCloseClick,
            modifier,
            content = content
        )
    } else {
        BasicDialog(
            title,
            onSaveClick,
            onCloseClick,
            modifier,
            maxHeightDp = 560.dp,
            content
        )
    }
}
