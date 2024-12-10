package com.ainsln.feature.notes.editor.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.core.ui.components.text.CombinedSectionText
import com.ainsln.core.ui.components.text.ExpandableSectionCard
import com.ainsln.core.ui.components.text.RemovableTextField
import com.ainsln.core.ui.components.text.SectionCard
import com.ainsln.core.ui.components.text.SectionSubtitle
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.editor.InterpretationFieldsModifier
import com.ainsln.feature.notes.state.NoteEditorUiState

@Composable
fun InterpretationTab(
    uiState: NoteEditorUiState,
    fieldsModifier: InterpretationFieldsModifier,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.fillMaxSize().padding(bottom = 12.dp)

    ){

        ThoughtsSection(
            uiState = uiState,
            onAddThought = fieldsModifier::addThought,
            onRemoveThought = fieldsModifier::removeThought,
            onUpdateThoughtText = fieldsModifier::updateThoughtText
        )

        DistortionsSection(
            uiState = uiState,
            toggleDistortionsDialog = fieldsModifier::toggleDistortionsDialog,
            onRemoveDistortion = fieldsModifier::removeDistortion,
        )

    }
}

@Composable
fun ThoughtsSection(
    uiState: NoteEditorUiState,
    onAddThought: () -> Unit,
    onRemoveThought: (Int) -> Unit,
    onUpdateThoughtText: (Int, String) -> Unit,
    modifier: Modifier = Modifier
){
    ExpandableSectionCard(
        title = stringResource(R.string.thoughts_label),
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        Column {
            SectionSubtitle(
                text = stringResource(R.string.thoughts_text),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            uiState.noteDetails.thoughts.forEachIndexed { index, thought ->
                RemovableTextField(
                    placeholder = stringResource(R.string.thought_number, index+1),
                    text = thought.text,
                    deleteContentDescription = stringResource(R.string.delete_thought),
                    onTextChanged = { onUpdateThoughtText(index, it) },
                    onDeleteClick = { onRemoveThought(index) }
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ){
                SmallFloatingActionButton(
                    onClick = { onAddThought() },
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_thought)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DistortionsSection(
    uiState: NoteEditorUiState,
    toggleDistortionsDialog: (Boolean) -> Unit,
    onRemoveDistortion: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = stringResource(R.string.distortions_label),
        trailingIcon = {
            IconButton(onClick = { toggleDistortionsDialog(true) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.select_distortions)
                )
            }
        },
        modifier = modifier
    ) { contentModifier ->
        Column(contentModifier) {
            if (uiState.noteDetails.distortions.isEmpty()) {
                CombinedSectionText(
                    text = stringResource(R.string.distortions_text),
                    boldText = stringResource(R.string.distortions_placeholder)
                )
            } else {
                SectionSubtitle(
                    text = stringResource(R.string.distortions_text)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy((-8).dp)
                ) {
                    uiState.noteDetails.distortions.forEach { distortion ->
                        InputChip(
                            selected = false,
                            onClick = {},
                            label = { Text(distortion.name) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Close,
                                    contentDescription = stringResource(R.string.delete_distortion),
                                    modifier = Modifier.clickable { onRemoveDistortion(distortion.id) }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ThoughtsTabPreview() {
    CBTJournalTheme {
        InterpretationTab(
            uiState = NoteEditorUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
