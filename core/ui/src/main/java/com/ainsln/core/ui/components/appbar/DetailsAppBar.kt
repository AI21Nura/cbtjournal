package com.ainsln.core.ui.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ainsln.core.ui.R
import com.ainsln.core.ui.theme.CBTJournalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsAppBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    canNavigateUp: Boolean = true,
    transparent: Boolean = false,
    actions: @Composable () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (canNavigateUp) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }

        },
        actions = {
            actions()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (transparent) Color.Transparent else Color.Unspecified
        ),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DetailsAppBarPreview(

) {
    CBTJournalTheme {
        DetailsAppBar(
            title = "New Note",
            onBack = {}
        ) {

        }
    }
}
