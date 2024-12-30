package com.ainsln.core.ui.components.appbar

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDestinationAppBar(
    title: String,
    alignCenter: Boolean = false,
    actions: (@Composable () -> Unit)? = null
){
    if (alignCenter){
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
            actions = { actions?.invoke() }
        )
    }
    else {
        TopAppBar(
            title = { Text(text = title)
            },
            actions = { actions?.invoke() }
        )
    }

}

