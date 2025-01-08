package com.ainsln.core.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.ainsln.core.ui.R

@Composable
fun BasicDialog(
    title: String,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    maxHeightDp: Dp = Dp.Unspecified,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onCloseClick
    ) {
        OutlinedCard(
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = modifier.verticalScroll(rememberScrollState())
        ) {
            BasicDialogHeader(title)
            Column(
                modifier = Modifier
                    .heightIn(max = maxHeightDp)
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
                    .zIndex(-1f)
            ) {
                content()
            }
            BasicDialogActionButtons(onCloseClick, onSaveClick)
        }
    }
}

@Composable
fun BasicDialogHeader(
    title: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardDefaults.outlinedCardColors().containerColor
){
    Row(
        modifier = modifier.fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun BasicDialogActionButtons(
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = CardDefaults.outlinedCardColors().containerColor
){
    HorizontalDivider(thickness = 0.5.dp)
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp, Alignment.End),
        modifier = modifier.fillMaxWidth()
            .background(backgroundColor)
            .padding(end = 24.dp)
    ) {
        TextButton(onClick = onCloseClick) {
            Text(stringResource(R.string.cancel_label))
        }

        TextButton(onClick = onSaveClick) {
            Text(stringResource(R.string.save_label))
        }
    }
}
