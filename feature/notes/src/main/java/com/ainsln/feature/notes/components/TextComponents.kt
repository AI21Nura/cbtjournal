package com.ainsln.feature.notes.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun InputTextField(
    subtitle: String,
    placeholder: String,
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 5,
    maxLines: Int = 10
) {
    Column(modifier) {
        SectionSubtitle(subtitle)

        OutlinedTextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            ),
            shape = RoundedCornerShape(25.dp),
            minLines = minLines,
            maxLines = maxLines,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)

        )
    }

}

@Composable
fun RemovableTextField(
    placeholder: String,
    text: String,
    onTextChanged: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { onTextChanged(it) },
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Remove distortion",
                    modifier = Modifier.clickable { onDeleteClick() }
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            ),
            minLines = 2,
            maxLines = 4,
            modifier = Modifier.weight(1f)

        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
){
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun SectionSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier
){
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
        textAlign = TextAlign.Justify,
        modifier = modifier
    )
}

@Composable
fun CombinedSectionText(
    text: String,
    boldText: String,
    modifier: Modifier = Modifier,
    boldFirst: Boolean = false,
    italic: Boolean = true
) {
    Text(

        buildAnnotatedString {
            if (boldFirst){
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(boldText)
                }
                append(text)
            } else {
                append(text)
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(boldText)
                }
            }
        },
        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal),
        textAlign = TextAlign.Justify,
        modifier = modifier
    )
}
