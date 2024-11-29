package com.ainsln.feature.distortions.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.Distortion
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.DistortionsPreviewData
import com.ainsln.feature.distortions.state.DistortionsListUiState


@Composable
internal fun DistortionsScreen(
    onDistortionClick: (Long) -> Unit,
    listState: LazyListState,
    viewModel: DistortionsViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DistortionsContent(
        uiState = uiState,
        onDistortionClick = onDistortionClick,
        listState = listState,
        contentPadding = contentPadding
    )
}

@Composable
internal fun DistortionsContent(
    uiState: DistortionsListUiState,
    onDistortionClick: (Long) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        when (uiState) {
            is UiState.Error -> {
                ErrorScreen(
                    message = uiState.e.message ?: "Error loading list of distortions",
                    contentPadding = contentPadding
                )
            }
            is UiState.Loading -> {
                LoadingScreen(contentPadding = contentPadding)
            }
            is UiState.Success -> {
                DistortionsList(
                    distortions = uiState.data,
                    onDistortionClick = onDistortionClick,
                    listState = listState,
                    contentPadding = contentPadding
                )
            }
        }
    }
}

@Composable
internal fun DistortionsList(
    distortions: List<Distortion>,
    onDistortionClick: (Long) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp)
) {

    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 24.dp,
                end = 24.dp
            )
    ) {
        items(items = distortions, key = { it.id }) { distortion ->
            DistortionItem(
                distortion = distortion,
                onDistortionClick = onDistortionClick
            )
        }
    }
}

@Composable
internal fun DistortionItem(
    distortion: Distortion,
    onDistortionClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
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
            Icon(imageVector = Icons.AutoMirrored.Outlined.NavigateNext, contentDescription = null)
        },
        shadowElevation = 4.dp,
        modifier = modifier
            .padding(vertical = 8.dp)
            .clickable { onDistortionClick(distortion.id) }
    )
}

@Preview(showBackground = true)
@Composable
internal fun DistortionItemPreview() {
    CBTJournalTheme {
        Surface {
            DistortionItem(
                distortion = DistortionsPreviewData.getDistortion(LocalContext.current),
                onDistortionClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun DistortionsListPreview() {
    CBTJournalTheme {
        Surface {
            DistortionsList(
                distortions = DistortionsPreviewData.getDistortionsList(LocalContext.current),
                onDistortionClick = { },
                listState = rememberLazyListState()
            )
        }
    }
}
