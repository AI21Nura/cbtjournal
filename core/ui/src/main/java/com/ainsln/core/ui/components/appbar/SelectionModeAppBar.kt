package com.ainsln.core.ui.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ainsln.core.ui.R
import com.ainsln.core.ui.theme.CBTJournalTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SelectionModeTopAppBar(
    selectedItemsNumber: Int,
    resetSelectionMode: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.selected_label, selectedItemsNumber),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(onClick = resetSelectionMode) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.reset_selection)
                )
            }
        },
        actions = {
            if (selectedItemsNumber > 0)
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_selected)
                    )
                }
        }
    )
}

@Preview
@Composable
fun SelectionModeTopAppBarPreview(){
    CBTJournalTheme {
        SelectionModeTopAppBar(
            selectedItemsNumber = 3,
            resetSelectionMode = {},
            onDeleteClick = {}
        )
    }
}
