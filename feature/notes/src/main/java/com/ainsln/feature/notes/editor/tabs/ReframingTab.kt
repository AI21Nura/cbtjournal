package com.ainsln.feature.notes.editor.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.Thought
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.core.ui.components.text.CombinedSectionText
import com.ainsln.core.ui.components.text.ExpandableSectionCard
import com.ainsln.core.ui.components.text.SectionSubtitle
import com.ainsln.core.ui.components.text.SectionText
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.editor.ReframingFieldsModifier
import com.ainsln.feature.notes.state.NoteEditorUiState

@Composable
fun ReframingTab(
    uiState: NoteEditorUiState,
    fieldsModifier: ReframingFieldsModifier,
    modifier: Modifier = Modifier
){
    Column(modifier.fillMaxSize().padding(bottom = 12.dp)){
        AlternativesSection(uiState, fieldsModifier::updateAlternativeThought)
        ReappraisalSection(uiState, fieldsModifier::updateEmotionIntensityAfter)
    }
}

@Composable
fun AlternativesSection(
    uiState: NoteEditorUiState,
    onUpdateAlternativeThought: (Int, String) -> Unit,
    modifier: Modifier = Modifier
){
    ExpandableSectionCard(
        title = stringResource(R.string.alternatives_label),
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column {
            if (uiState.noteDetails.thoughts.isEmpty()){
                CombinedSectionText(
                    text = stringResource(R.string.alternatives_text),
                    boldText = stringResource(R.string.alternatives_placeholder)
                )
            } else {
                SectionSubtitle(
                    text = stringResource(R.string.alternatives_text),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                uiState.noteDetails.thoughts.forEachIndexed { index, thought ->
                    if (thought.text.isNotBlank()) {
                        AlternativeThoughtInput(
                            index = index,
                            thought = thought,
                            onUpdateAlternativeThought = onUpdateAlternativeThought
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun AlternativeThoughtInput(
    index: Int,
    thought: Thought,
    onUpdateAlternativeThought: (Int, String) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier.padding(vertical = 8.dp)) {
        HorizontalDivider(Modifier.padding(bottom = 16.dp))
        CombinedSectionText(
            text = thought.text,
            boldText = stringResource(R.string.thought_number, index+1),
            boldFirst = true,
            italic = false
        )
        OutlinedTextField(
            value = thought.alternativeThought,
            onValueChange = { onUpdateAlternativeThought(index, it) },
            placeholder = {
                Text(
                    text = stringResource(R.string.text_placeholder),
                    style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.outline
                )
            },
            minLines = 1,
            maxLines = 4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
fun ReappraisalSection(
    uiState: NoteEditorUiState,
    onIntensityChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
){
    ExpandableSectionCard(
        title = stringResource(R.string.emotions_re_label),
        modifier = modifier.padding(vertical = 4.dp)
    ){
        Column(modifier) {
            SectionSubtitle(
                text = stringResource(R.string.emotions_re_text),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            uiState.noteDetails.emotions.forEachIndexed { index, emotion ->
                ReappraisalRow(index, emotion, onIntensityChanged)
            }
        }
    }
}

@Composable
fun ReappraisalRow(
    index: Int,
    selectedEmotion: SelectedEmotion,
    onIntensityChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column(modifier) {
        HorizontalDivider(Modifier.padding(vertical = 4.dp))
        Text(
            text = selectedEmotion.emotion.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SectionText(stringResource(R.string.before_text, selectedEmotion.intensityBefore))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            SectionText(
                text = stringResource(R.string.after_text, selectedEmotion.intensityAfter),
                modifier = Modifier.widthIn(min = 112.dp).padding(end = 8.dp)
            )

            Slider(
                value = selectedEmotion.intensityAfter.toFloat(),
                onValueChange = { onIntensityChanged(index, it.toInt()) },
                valueRange = 0f..100f,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReframingTabPreview() {
    CBTJournalTheme {
        ReframingTab(
            uiState = NoteEditorUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
