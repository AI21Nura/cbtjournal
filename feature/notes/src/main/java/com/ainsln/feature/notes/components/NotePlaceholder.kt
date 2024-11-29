package com.ainsln.feature.notes.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ainsln.core.ui.components.AppPlaceholder
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.feature.notes.R

@Composable
internal fun NoteDetailsPlaceholder(
    modifier: Modifier = Modifier
) {
    AppPlaceholder(
        text = "Select a note to view details",
        icon = painterResource(R.drawable.ic_note_placeholder),
        modifier = modifier
    )
}

@Composable
internal fun EmptyNotesList(
    modifier: Modifier = Modifier
){
    ErrorScreen(
        message = "The list is empty. Add your first note!",
        icon = painterResource(R.drawable.ic_no_notes),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
internal fun NoteDetailsPlaceholderPreview() {
    CBTJournalTheme{
        NoteDetailsPlaceholder()
    }
}
