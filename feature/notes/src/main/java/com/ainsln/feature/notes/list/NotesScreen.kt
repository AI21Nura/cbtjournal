package com.ainsln.feature.notes.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.ShortNote
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.components.appbar.TopDestinationAppBar
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.components.EmptyNotesList
import com.ainsln.feature.notes.state.NotesListUiState
import com.ainsln.feature.notes.utils.formatDate

@Composable
fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NotesContent(
        uiState,
        onNoteClick,
        onAddNoteClick,
        contentPadding
    )
}

@Composable
internal fun NotesContent(
    uiState: NotesListUiState,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    contentPadding: PaddingValues
) {
    when (uiState) {
        is UiState.Success -> {
            NotesContent(
                uiState.data,
                onNoteClick,
                onAddNoteClick,
                contentPadding
            )
        }

        is UiState.Loading -> {
            LoadingScreen(contentPadding = contentPadding)
        }

        is UiState.Error -> {
            ErrorScreen(
                message = uiState.e.message ?: "Error loading details",
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
internal fun NotesContent(
    notes: List<ShortNote>,
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopDestinationAppBar("Notes")
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNoteClick) {
                Icon(Icons.Filled.Add, "Add note")
            }
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            NotesListContent(
                notes = notes,
                contentPadding = contentPadding,
                onNoteClick = onNoteClick
            )
        }
    }
}

@Composable
internal fun NotesListContent(
    notes: List<ShortNote>,
    onNoteClick: (Long) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    if (notes.isNotEmpty()) {
        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            items(notes) { note ->
                NoteListItem(note, onNoteClick)
            }
        }
    } else {
        EmptyNotesList()
    }
}

@Composable
internal fun NoteListItem(
    note: ShortNote,
    onNoteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = note.situation,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            },
            supportingContent = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(formatDate(note.date))
                    EmotionsList(note.emotions)
                }
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clickable { onNoteClick(note.id) }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun EmotionsList(emotions: List<SelectedEmotion>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        emotions.forEach { emotion ->
            OutlinedCard(
                colors = CardDefaults.cardColors(
                    containerColor = Color(emotion.emotion.color)
                )
            ) {
                Text(
                    text = emotion.emotion.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotesScreenPreview() {
    CBTJournalTheme {
        NotesContent(
            onAddNoteClick = {},
            onNoteClick = {},
            notes = NotesPreviewData.shortNotes,
            contentPadding = PaddingValues(12.dp)
        )
    }
}