package com.ainsln.feature.notes.entry.tabs

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
import com.ainsln.feature.notes.entry.SituationFieldsModifier
import com.ainsln.feature.notes.state.NoteEntryUiState
import com.ainsln.feature.notes.utils.convertMillisToDate

@Composable
fun SituationTab(
    uiState: NoteEntryUiState,
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
            title = "Situation",
            modifier = Modifier.padding(vertical = 4.dp)
        ) { modifier ->
            InputTextField(
                subtitle = "Describe the event or situation that triggered an emotional reaction in you. Be specific and objective in your description.",
                placeholder = "Enter text...",
                text = uiState.noteDetails.situation,
                onTextChanged = { fieldsModifier.updateTextField(uiState.noteDetails.copy(situation = it)) },
                modifier = modifier
            )
        }

        ExpandableSectionCard(
            title = "Reaction",
            modifier = Modifier.padding(vertical = 4.dp)
        ) { modifier ->
            InputTextField(
                subtitle = "Describe your physical sensations in your body.",
                placeholder = "Enter text...",
                text = uiState.noteDetails.bodyReaction,
                onTextChanged = { fieldsModifier.updateTextField(uiState.noteDetails.copy(bodyReaction = it)) },
                minLines = 3,
                modifier = modifier
            )
            InputTextField(
                subtitle = "Describe how you reacted to this situation.",
                placeholder = "Enter text...",
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
    uiState: NoteEntryUiState,
    toggleEmotionsDialog: (Boolean) -> Unit,
    onIntensityChanged: (Int, Int) -> Unit,
    onRemoveEmotion: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    SectionCard(
        title = "Emotions",
        trailingIcon = {
            IconButton(onClick = { toggleEmotionsDialog(true) }) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Select distortions"
                )
            }
        },
        modifier = modifier
    ) { contentModifier ->
        Column(contentModifier) {
            if (uiState.noteDetails.emotions.isEmpty()) {
                CombinedSectionText(
                    text = "Select emotions that you can notice in your thoughts. ",
                    boldText = "Click on the pencil icon to open the list of emotions."
                )
            } else {
                SectionSubtitle(
                    subtitle = "Select emotions that you can notice in your thoughts."
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
            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete emotion")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SituationTabPreview() {
    CBTJournalTheme {
        SituationTab(
            uiState = NoteEntryUiState(),
            fieldsModifier = NotesPreviewData.fieldsModifier,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}
