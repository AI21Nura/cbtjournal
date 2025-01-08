package com.ainsln.feature.notes.details

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.Distortion
import com.ainsln.core.model.Note
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.Thought
import com.ainsln.core.ui.components.RenderUiStateScaffold
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.components.dialog.NoteAlertDialog
import com.ainsln.core.ui.components.text.CombinedSectionText
import com.ainsln.core.ui.components.text.SectionSubtitle
import com.ainsln.core.ui.components.text.SectionText
import com.ainsln.core.ui.components.text.SectionTitle
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.state.ActionState
import com.ainsln.feature.notes.state.NoteDetailsUiState
import com.ainsln.feature.notes.utils.formatDate

@Composable
fun NoteDetailsScreen(
    onEditClick: (Long) -> Unit,
    onBack: () -> Unit,
    canNavigateUp: Boolean,
    viewModel: NoteDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.noteState.collectAsStateWithLifecycle()
    val deleteState by viewModel.deleteState.collectAsStateWithLifecycle()

    NoteDetailsContent(
        uiState = uiState,
        onEditClick = onEditClick,
        deleteState = deleteState,
        onBack = onBack,
        canNavigateUp = canNavigateUp,
        onDeleteClick = viewModel::deleteNote,
        shareNote = viewModel::shareNote,
        resetDeleteState = viewModel::resetDeleteState
    )
}

@Composable
internal fun NoteDetailsContent(
    uiState: NoteDetailsUiState,
    onEditClick: (Long) -> Unit,
    deleteState: ActionState<Unit>,
    onBack: () -> Unit,
    resetDeleteState: () -> Unit,
    canNavigateUp: Boolean,
    onDeleteClick: (Note) -> Unit,
    shareNote: (Note, Context) -> Unit,
) {
    RenderUiStateScaffold(
        uiState = uiState,
        topBarTitle = R.string.notes_detail_title,
        errMsgRes = R.string.notes_details_error,
        onBack = onBack,
        canNavigateUp = canNavigateUp
    ){ data ->
        NoteDetailsContent(
            note = data,
            deleteState = deleteState,
            resetDeleteState = resetDeleteState,
            onEditClick = onEditClick,
            onDeleteClick = onDeleteClick,
            onBack = onBack,
            canNavigateBack = canNavigateUp,
            shareNote = shareNote,
        )
    }
}

@Composable
internal fun NoteDetailsContent(
    note: Note,
    deleteState: ActionState<Unit>,
    resetDeleteState: () -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Note) -> Unit,
    onBack: () -> Unit,
    canNavigateBack: Boolean,
    shareNote: (Note, Context) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(deleteState) {
        if (deleteState is ActionState.Error) {
            snackbarHostState.showSnackbar(message = context.getString(R.string.cant_delete_note))
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            DetailsAppBar(
                title = stringResource(R.string.notes_detail_title),
                onBack = onBack,
                canNavigateUp = canNavigateBack
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        shareNote(note, context)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = stringResource(R.string.share_note)
                        )
                    }
                    IconButton(onClick = { onEditClick(note.id) }) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit_note)
                        )
                    }
                    DeleteIcon(
                        deleteState = deleteState,
                        resetDeleteState = resetDeleteState,
                        openDeleteDialog = { isDeleteDialogOpen = true },
                        onBack = onBack
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier.padding(innerPadding)) {
            NoteDetails(note = note)
        }
    }

    if (isDeleteDialogOpen) {
        NoteAlertDialog(
            title = stringResource(R.string.delete_note),
            text = stringResource(R.string.delete_warning),
            onDismissClick = { isDeleteDialogOpen = false },
            onConfirmClick = {
                onDeleteClick(note)
                isDeleteDialogOpen = false
            }
        )
    }
}

@Composable
internal fun DeleteIcon(
    deleteState: ActionState<Unit>,
    resetDeleteState: () -> Unit,
    openDeleteDialog: () -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.size(48.dp)) {
        when (deleteState) {
            is ActionState.Idle, is ActionState.Error -> {
                IconButton(onClick = { openDeleteDialog() }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete_note)
                    )
                }
            }
            is ActionState.Loading -> CircularProgressIndicator(Modifier.padding(8.dp))
            is ActionState.Success -> {
                resetDeleteState()
                onBack()
            }
        }
    }
}

@Composable
internal fun NoteDetails(
    note: Note,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        DetailsSection(title = stringResource(R.string.date_label), content = {
            SectionText(formatDate(note.date))
        })
        DetailsSection(title = stringResource(R.string.situation_label),
            isEmpty = note.situation.isBlank(),
            content = {
                SectionText(note.situation)
            })
        ThoughtsSection(note.thoughts)
        DistortionsSection(note.distortions)

        ReactionSection(
            bodyReaction = note.bodyReaction,
            behavioralReaction = note.behavioralReaction
        )
        EmotionsSection(note.emotions)
    }
}

@Composable
internal fun DetailsSection(
    title: String,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isEmpty: Boolean = false
) {
    Column(modifier.padding(bottom = 8.dp)) {
        SectionTitle(title = title)
        if (isEmpty) SectionSubtitle(stringResource(R.string.not_filled))
        else content()
    }
}

@Composable
internal fun EmotionsSection(
    emotions: List<SelectedEmotion>,
    modifier: Modifier = Modifier
) {
    DetailsSection(title = stringResource(R.string.emotions_label),
        isEmpty = emotions.isEmpty(),
        content = {
            Column(modifier) {
                emotions.forEach { emotion -> EmotionRow(emotion) }
            }
        })
}

@Composable
internal fun EmotionRow(
    emotion: SelectedEmotion,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            CombinedSectionText(
                text = "${emotion.intensityBefore}% → ${emotion.intensityAfter}%",
                boldText = "${emotion.emotion.name}: ", boldFirst = true
            )
        }

    }
}

@Composable
internal fun ThoughtsSection(
    thoughts: List<Thought>,
    modifier: Modifier = Modifier
) {
    DetailsSection(
        title = stringResource(R.string.thoughts_and_alternatives_label),
        isEmpty = thoughts.isEmpty(),
        content = {
            Column(modifier) {
                thoughts.forEachIndexed { index, thought ->
                    ThoughtRow(thought, index)
                }
            }
        })
}

@Composable
internal fun ThoughtRow(
    thought: Thought,
    index: Int,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(top = 8.dp)
    ) {
        CombinedSectionText(
            text = thought.text,
            boldText = stringResource(R.string.thought_number, index+1),
            boldFirst = true,
            italic = false,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        OutlinedCard(modifier.fillMaxWidth()) {
            CombinedSectionText(
                text = thought.alternativeThought,
                boldText = stringResource(R.string.alternative),
                boldFirst = true,
                italic = false,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun DistortionsSection(
    distortions: List<Distortion>,
    modifier: Modifier = Modifier
) {
    DetailsSection(
        title = stringResource(R.string.distortions_label),
        isEmpty = distortions.isEmpty(),
        content = {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = modifier.padding(top = 4.dp)
            ) {
                distortions.forEach { distortion ->
                    DistortionRow(distortion)
                }
            }
        })
}

@Composable
internal fun DistortionRow(
    distortion: Distortion,
    modifier: Modifier = Modifier
) {
    OutlinedCard {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(text = distortion.name, style = MaterialTheme.typography.bodyMedium)
            BasicTooltip(distortion.shortDescription)
        }
    }
}

@Composable
internal fun ReactionSection(
    bodyReaction: String,
    behavioralReaction: String,
    modifier: Modifier = Modifier
) {
    DetailsSection(
        title = stringResource(R.string.reaction_label),
        isEmpty = bodyReaction.isBlank() && behavioralReaction.isBlank(),
        content = {
            if (bodyReaction.isNotBlank()) {
                SectionSubtitle(
                    text = stringResource(R.string.body_reaction_label),
                    modifier = modifier.padding(vertical = 4.dp)
                )
                SectionText(text = bodyReaction)
            }
            if (behavioralReaction.isNotBlank()) {
                SectionSubtitle(
                    text = stringResource(R.string.behavioral_reaction_label),
                    modifier = modifier.padding(vertical = 4.dp)
                )
                SectionText(text = behavioralReaction)
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicTooltip(
    text: String
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(caretSize = DpSize(24.dp, 12.dp)) { Text(text) }
        },
        state = rememberTooltipState(isPersistent = false)
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.info),
            tint = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun NoteDetailsPreview() {
    CBTJournalTheme {
        NoteDetailsContent(
            note = NotesPreviewData.note,
            onEditClick = {},
            onBack = {},
            canNavigateBack = true,
            onDeleteClick = {},
            deleteState = ActionState.Idle,
            resetDeleteState = {},
            shareNote = { _, _ -> }
        )
    }
}
