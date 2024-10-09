package com.ainsln.feature.distortions.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.Distortion
import com.ainsln.core.resources.R
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.components.dialog.AppDialog
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.theme.SelectedItem
import com.ainsln.core.ui.utils.MultiSelectionDialogArgs
import com.ainsln.feature.distortions.list.DistortionsViewModel
import com.ainsln.feature.distortions.state.DistortionUiState
import com.ainsln.feature.distortions.state.DistortionsListUiState

@Composable
fun MultiSelectionDialog(
    args: MultiSelectionDialogArgs,
    viewModel: DistortionsViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDistortions = remember {
        mutableStateListOf<Long>().apply { addAll(args.initListIds) }
    }

    AppDialog(
        title = "Select distortions",
        onSaveClick = {
            args.callbacks.onSave(selectedDistortions)
        },
        onCloseClick = args.callbacks::onClose
    ) {
        MultiSelectionDialogContent(
            uiState = uiState,
            selectedDistortions = selectedDistortions,
            onSelectClick = { id, isChecked ->
                if (isChecked) selectedDistortions.add(id)
                else selectedDistortions.remove(id)
            }
        )
    }
}

@Composable
internal fun MultiSelectionDialogContent(
    uiState: DistortionsListUiState,
    selectedDistortions: List<Long>,
    onSelectClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when(uiState){
            is DistortionUiState.Success -> {
                MultiSelectionList(
                    distortions = uiState.data,
                    selectedDistortions = selectedDistortions,
                    onSelectClick = onSelectClick
                )
            }
            is DistortionUiState.Loading -> {
                LoadingScreen()
            }
            is DistortionUiState.Error -> {
                ErrorScreen(message = uiState.e.message ?: "Error loading list of distortions")
            }
        }
    }
}

@Composable
internal fun MultiSelectionList(
    distortions: List<Distortion>,
    selectedDistortions: List<Long>,
    onSelectClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(distortions){ distortion ->
            MultiSelectionListItem(
                distortion = distortion,
                isChecked = selectedDistortions.contains(distortion.id),
                onCheckedChange = { checked -> onSelectClick(distortion.id, checked) }
            )
        }
    }
}

@Composable
internal fun MultiSelectionListItem(
    distortion: Distortion,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        ListItem(
            leadingContent = {
                Icon(
                    painter = painterResource(distortion.iconResId),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(48.dp)
                )
            },
            headlineContent = {
                Text(text = distortion.name)
            },
            supportingContent = {
                Text(text = distortion.shortDescription)
            },
            trailingContent = {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onCheckedChange(!isChecked) }
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = if (isChecked) SelectedItem else Color.Unspecified
            ),
            modifier = modifier.clickable { onCheckedChange(!isChecked) }
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun MultiSelectionListPreview(){
    CBTJournalTheme {
        MultiSelectionList(
            distortions = listOf(
                Distortion(
                    id = 1,
                    name = "Labeling",
                    shortDescription = "You judge yourself or others using harsh and negative labels.",
                    longDescription = "Labeling occurs when you make generalized and categorical conclusions about yourself or others based on a single action or situation. Instead of viewing behavior as an isolated act, you define your identity or others through labels such as \"loser,\" \"stupid,\" or \"lazy.\" This can lead to low self-esteem and persistent negative thoughts.",
                    examples = listOf(
                        "After making a mistake at work, you tell yourself, \"I'm a total failure\".",
                        "After being late for a meeting, you think, \"I'm always irresponsible.\"",
                        "If a friend cancels plans once, your immediate thought is, \"They're a terrible friend; they don't care about me."
                    ),
                    iconResId = R.drawable.ic_distortion_1
                ),
                Distortion(
                    id = 2,
                    name = "Disqualifying the Positive",
                    shortDescription = "You ignore or diminish the importance of positive events, attributing them to luck.",
                    longDescription = "Labeling occurs when you make generalized and categorical conclusions about yourself or others based on a single action or situation. Instead of viewing behavior as an isolated act, you define your identity or others through labels such as \"loser,\" \"stupid,\" or \"lazy.\" This can lead to low self-esteem and persistent negative thoughts.",
                    examples = listOf(
                        "After making a mistake at work, you tell yourself, \"I'm a total failure\".",
                        "After being late for a meeting, you think, \"I'm always irresponsible.\"",
                        "If a friend cancels plans once, your immediate thought is, \"They're a terrible friend; they don't care about me."
                    ),
                    iconResId = R.drawable.ic_distortion_2
                ),
            ),
            selectedDistortions = listOf(1),
            onSelectClick = { id, checked ->

            }
        )
    }
}
