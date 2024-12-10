package com.ainsln.feature.notes.editor.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.core.ui.components.text.CombinedSectionText
import com.ainsln.feature.notes.components.DateSection
import com.ainsln.core.ui.components.text.ExpandableSectionCard
import com.ainsln.core.ui.components.text.InputTextField
import com.ainsln.core.ui.components.text.SectionCard
import com.ainsln.core.ui.components.text.SectionSubtitle
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.editor.SituationFieldsModifier
import com.ainsln.feature.notes.state.NoteEditorUiState
import com.ainsln.feature.notes.utils.convertMillisToDate

@Composable
fun SituationTab(
    uiState: NoteEditorUiState,
    fieldsModifier: SituationFieldsModifier,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize().padding(bottom = 12.dp)
    ) {
        DateSection(
            selectedDate = convertMillisToDate(uiState.noteDetails.date.time),
            onDateChanged = { it?.let(fieldsModifier::updateDate) }
        )

        ExpandableSectionCard(
            title = stringResource(R.string.situation_label),
            modifier = Modifier.padding(vertical = 4.dp)
        ) { modifier ->
            InputTextField(
                subtitle = stringResource(R.string.situation_text),
                placeholder = stringResource(R.string.text_placeholder),
                text = uiState.noteDetails.situation,
                onTextChanged = { fieldsModifier.updateTextField(uiState.noteDetails.copy(situation = it)) },
                modifier = modifier
            )
        }

        ExpandableSectionCard(
            title = stringResource(R.string.reaction_label),
            modifier = Modifier.padding(vertical = 4.dp)
        ) { modifier ->
            InputTextField(
                subtitle = stringResource(R.string.body_reaction_text),
                placeholder = stringResource(R.string.text_placeholder),
                text = uiState.noteDetails.bodyReaction,
                onTextChanged = { fieldsModifier.updateTextField(uiState.noteDetails.copy(bodyReaction = it)) },
                minLines = 3,
                modifier = modifier
            )
            InputTextField(
                subtitle = stringResource(R.string.behavioral_reaction_text),
                placeholder = stringResource(R.string.text_placeholder),
                text = uiState.noteDetails.behavioralReaction,
                onTextChanged = { fieldsModifier.updateTextField(uiState.noteDetails.copy(behavioralReaction = it)) },
                minLines = 3,
                modifier = modifier.padding(top = 16.dp)
            )
        }

        EmotionsSection(
            uiState = uiState,
            toggleEmotionsDialog = fieldsModifier::toggleEmotionsDialog,
            onIntensityChanged = fieldsModifier::updateEmotionIntensityBefore,
            onRemoveEmotion = fieldsModifier::removeEmotion
        )
    }
}

@Composable
fun EmotionsSection(
    uiState: NoteEditorUiState,
    toggleEmotionsDialog: (Boolean) -> Unit,
    onIntensityChanged: (Int, Int) -> Unit,
    onRemoveEmotion: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    SectionCard(
        title = stringResource(R.string.emotions_label),
        trailingIcon = {
            IconButton(onClick = { toggleEmotionsDialog(true) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = stringResource(R.string.select_emotions)
                )
            }
        },
        modifier = modifier
    ) { contentModifier ->
        Column(contentModifier) {
            if (uiState.noteDetails.emotions.isEmpty()) {
                CombinedSectionText(
                    text = stringResource(R.string.emotions_text),
                    boldText = stringResource(R.string.emotions_placeholder)
                )
            } else {
                SectionSubtitle(
                    text = stringResource(R.string.emotions_text)
                )
                Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    uiState.noteDetails.emotions.forEachIndexed { index, selectedEmotion ->
                        EmotionRow(
                            index,
                            selectedEmotion,
                            onIntensityChanged,
                            onRemoveEmotion
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmotionRow(
    index: Int,
    selectedEmotion: SelectedEmotion,
    onIntensityChanged: (Int, Int) -> Unit,
    onRemoveEmotion: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ){

        Text(
            text = "${selectedEmotion.emotion.name}: ${selectedEmotion.intensityBefore}%"
        )

        Slider(
            value = selectedEmotion.intensityBefore.toFloat(),
            onValueChange = { onIntensityChanged(index, it.toInt()) },
            valueRange = 0f..100f,
            modifier = Modifier.padding(start = 8.dp).weight(1f)
        )

        IconButton(onClick = { onRemoveEmotion(index) }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete_emotion)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SituationTabPreview() {
    CBTJournalTheme {
        SituationTab(
            uiState = NoteEditorUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
