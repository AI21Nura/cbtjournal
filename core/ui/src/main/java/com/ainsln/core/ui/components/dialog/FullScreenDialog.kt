package com.ainsln.core.ui.components.dialog

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ainsln.core.ui.R


@Composable
fun FullScreenDialog(
    title: String,
    onSaveClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        BackHandler { onCloseClick() }
        Surface {
            Column(modifier.padding(contentPadding)) {
                FullScreenDialogHeader(title, onCloseClick, onSaveClick)
                Column(Modifier.padding(vertical = 8.dp)) {
                    content()
                }
            }
        }
    }
}

@Composable
fun FullScreenDialogHeader(
    title: String,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.cancel_label),
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = onSaveClick) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = stringResource(R.string.save_label)
            )
        }
    }
}


