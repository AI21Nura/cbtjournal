package com.ainsln.feature.distortions.details

import android.graphics.Color.parseColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.ainsln.core.ui.components.AppPlaceholder
import com.ainsln.core.ui.components.RenderUiState
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.core.ui.theme.Ocean
import com.ainsln.core.ui.theme.Shadow
import com.ainsln.data.DistortionsPreviewData
import com.ainsln.feature.distortions.R
import com.ainsln.feature.distortions.state.DistortionDetailUiState


@Composable
internal fun DistortionDetailsScreen(
    canNavigateUp: Boolean,
    onBack: () -> Unit,
    viewModel: DistortionsDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.distortion.collectAsStateWithLifecycle()
    DistortionDetailsContent(
        uiState = uiState,
        canNavigateUp = canNavigateUp,
        onBack = onBack
    )
}

@Composable
internal fun DistortionDetailsContent(
    uiState: DistortionDetailUiState,
    canNavigateUp: Boolean,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            DetailsAppBar(
                title = "",
                onBack = onBack,
                canNavigateUp = canNavigateUp,
                transparent = true
            ) {}
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize()) {
            RenderUiState(
                uiState = uiState,
                errMsgRes = R.string.distortions_details_error,
                contentPadding = innerPadding
            ) { data ->
                DistortionDetails(distortion = data)
            }
        }
    }
}

@Composable
internal fun DistortionDetails(
    distortion: Distortion,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DistortionHeader(distortion)
        DistortionBody(distortion)
    }
}

@Composable
internal fun DistortionBody(
    distortion: Distortion,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(contentPadding)
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

@Composable
fun DistortionHeader(
    distortion: Distortion
) {
    val brush = Brush.verticalGradient(
        listOf(
            Color(parseColor("#c6f8ff")).copy(alpha = 0.05f),
            Color(parseColor("#c6f8ff")).copy(alpha = 0.1f),
            Color(parseColor("#8da7fa")).copy(alpha = 0.4f),
            Color(parseColor("#8297f9")).copy(alpha = 0.7f),
            Color(parseColor("#6f7bf7")).copy(alpha = 0.9f),
        )
    )
    OutlinedCard(
        border = BorderStroke(width = 0.dp, Color.Unspecified),
        elevation = CardDefaults.elevatedCardElevation(16.dp),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp,
            bottomStart = 25.dp,
            bottomEnd = 25.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(brush)
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
                style = MaterialTheme.typography.headlineSmall
                    .copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
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
internal fun ExampleItem(
    example: String,
    modifier: Modifier = Modifier
){
    Row(modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowRight,
            contentDescription = null,
            tint = Shadow
        )
        Text(text = example, textAlign = TextAlign.Justify)
    }
}

@Composable
internal fun DistortionDetailsPlaceholder(
    modifier: Modifier = Modifier
) {
    AppPlaceholder(
        text = stringResource(R.string.distortions_placeholder),
        icon = painterResource(R.drawable.ic_distortion_placeholder),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
internal fun DistortionHeaderPreview(){
    CBTJournalTheme {
        DistortionHeader(
            distortion = DistortionsPreviewData.getDistortion(LocalContext.current)
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun DistortionDetailPreview() {
    CBTJournalTheme {
        DistortionDetails(
            distortion = DistortionsPreviewData.getDistortion(LocalContext.current)
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
