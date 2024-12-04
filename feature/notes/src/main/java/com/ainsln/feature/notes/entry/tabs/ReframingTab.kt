package com.ainsln.feature.notes.entry.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.ainsln.feature.notes.entry.ReframingFieldsModifier
import com.ainsln.feature.notes.state.NoteEntryUiState

@Composable
fun ReframingTab(
    uiState: NoteEntryUiState,
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
    uiState: NoteEntryUiState,
    onUpdateAlternativeThought: (Int, String) -> Unit,
    modifier: Modifier = Modifier
){
    ExpandableSectionCard(
        title = "Automatic Thoughts",
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Column {
            if (uiState.noteDetails.thoughts.isEmpty()){
                CombinedSectionText(
                    text = "Describe your automatic negative thoughts. ",
                    boldText = "You need to add automatic thoughts in the interpretation tab."
                )
            } else {
                SectionSubtitle(
                    subtitle = "Describe your automatic negative thoughts.",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                uiState.noteDetails.thoughts.forEachIndexed { index, thought ->
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
            boldText = "Thought ${index+1}: ",
            boldFirst = true,
            italic = false
        )
        OutlinedTextField(
            value = thought.alternativeThought,
            onValueChange = { onUpdateAlternativeThought(index, it) },
            placeholder = {
                Text(
                    text = "Enter text...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Gray,
                focusedBorderColor = Color.Gray
            ),
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
    uiState: NoteEntryUiState,
    onIntensityChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
){
    ExpandableSectionCard(
        title = "Emotional reappraisal",
        modifier = modifier.padding(vertical = 4.dp)
    ){
        Column(modifier) {
            SectionSubtitle(
                subtitle = "Rate the intensity of negative emotions after reframing your thoughts.",
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

        Text(text = "Before: ${selectedEmotion.intensityBefore}%")
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "After: ${selectedEmotion.intensityAfter}%",
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
            uiState = NoteEntryUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
