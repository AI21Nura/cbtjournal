package com.ainsln.core.ui.components.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopDestinationAppBar(
    title: String,
    alignCenter: Boolean = false
){
    TopAppBar(
        title = {
            Text(
                text = title,
                textAlign = if (alignCenter) TextAlign.Center else TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}
