package com.ainsln.feature.distortions.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.Distortion
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.theme.Ocean
import com.ainsln.core.ui.theme.Shadow
import com.ainsln.feature.distortions.R
import com.ainsln.feature.distortions.state.DistortionDetailUiState
import com.ainsln.feature.distortions.state.DistortionUiState


@Composable
internal fun DistortionDetailsScreen(
    viewModel: DistortionsDetailsViewModel = hiltViewModel(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val uiState by viewModel.distortion.collectAsStateWithLifecycle()
    DistortionDetailsContent(
        uiState = uiState,
        contentPadding = contentPadding
    )
}

@Composable
internal fun DistortionDetailsContent(
    uiState: DistortionDetailUiState,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    when (uiState) {
        is DistortionUiState.Loading -> {
            LoadingScreen(contentPadding = contentPadding)
        }
        is DistortionUiState.Error -> {
            ErrorScreen(
                message = uiState.e.message ?: "Error loading details",
                contentPadding = contentPadding
            )
        }
        is DistortionUiState.Success -> {
            DistortionDetails(
                distortion = uiState.data,
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
internal fun DistortionDetails(
    distortion: Distortion,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current)
            ).verticalScroll(rememberScrollState())
    ) {
        DistortionHeader(distortion)
        DistortionBody(distortion)
    }
}

@Composable
internal fun DistortionHeader(
    distortion: Distortion,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawWithCache {
                val brush = Brush.linearGradient(
                    listOf(Ocean, Shadow),
                    tileMode = TileMode.Mirror
                )
                onDrawBehind {
                    drawHeaderBackground(size, brush, CornerRadius(65f, 65f))
                }
            }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(distortion.iconResId),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(128.dp)
            )
            Text(
                text = distortion.name,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = distortion.shortDescription,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
internal fun DistortionBody(
    distortion: Distortion,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {

        TitleText(stringResource(R.string.about))
        Text(
            text = distortion.longDescription,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Ocean)
        TitleText(stringResource(R.string.examples))
        distortion.examples.forEach { example ->
            ExampleItem(example)

        }
    }
}

@Composable
internal fun TitleText(
    text: String,
    modifier: Modifier = Modifier
){
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(bottom = 4.dp)
    )
}

internal fun DrawScope.drawHeaderBackground(size: Size, brush: Brush, cornerRadius: CornerRadius){
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = Offset(0f, 0f),
                    size = size,
                ),
                bottomLeft = cornerRadius,
                bottomRight = cornerRadius,
            )
        )
    }
    drawPath(path, brush, alpha = 0.8f)
}

@Composable
internal fun ExampleItem(
    example: String,
    modifier: Modifier = Modifier
){
    Row(modifier) {
        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowRight, contentDescription = null, tint = Shadow)
        Text(text = example, textAlign = TextAlign.Justify)
    }
}

@Composable
internal fun DistortionDetailsPlaceholder(
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 12.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.FormatListBulleted,
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )
            Text("Select a distortion from list")
        }
    }

}

@Preview(showBackground = true)
@Composable
internal fun DistortionDetailPreview() {
    CBTJournalTheme {
        DistortionDetails(
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
                iconResId = com.ainsln.core.resources.R.drawable.ic_distortion_1
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun DistortionDetailsPlaceholderPreview() {
    CBTJournalTheme{
        DistortionDetailsPlaceholder()
    }
}
