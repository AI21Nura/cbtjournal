package com.ainsln.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ainsln.core.ui.R
import com.ainsln.core.ui.theme.CBTJournalTheme

@Composable
fun ErrorScreen(
    message: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = contentPadding.calculateTopPadding()
            )
    ) {
        Icon(
            painter = icon ?: painterResource(R.drawable.ic_error),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(112.dp)
                .padding(bottom = 16.dp)
        )
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview(){
    CBTJournalTheme {
        Surface {
            ErrorScreen(message = "Error loading information")
        }
    }
}
