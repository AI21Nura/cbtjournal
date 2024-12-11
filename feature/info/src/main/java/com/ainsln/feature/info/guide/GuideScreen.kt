package com.ainsln.feature.info.guide

import android.graphics.Color.parseColor
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.GuideContent
import com.ainsln.core.ui.components.RenderUiState
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.components.text.CombinedSectionText
import com.ainsln.core.ui.components.text.ExpandableSectionCard
import com.ainsln.core.ui.components.text.SectionTitle
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.InfoPreviewData
import com.ainsln.feature.info.R
import com.ainsln.feature.info.state.GuideUiState

@Composable
fun GuideScreen(
    canNavigateUp: Boolean,
    onBack: () -> Unit,
    viewModel: GuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    GuideScreenContent(uiState, canNavigateUp, onBack)
}

@Composable
fun GuideScreenContent(
    uiState: GuideUiState,
    canNavigateUp: Boolean,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            DetailsAppBar(
                title = stringResource(R.string.guide_title),
                onBack = { onBack() },
                canNavigateUp = canNavigateUp
            ) {}
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            RenderUiState(
                uiState = uiState,
                errMsgRes = R.string.guide_error
            ) { data ->
                GuideBlock(data)
            }
        }
    }
}

@Composable
fun GuideBlock(
    guide: GuideContent,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp, end = 16.dp, bottom = 16.dp
    )
) {
    val colors = guide.exampleColors.map { Color(parseColor(it)) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = guide.intro, textAlign = TextAlign.Justify)

        guide.examplesList.forEachIndexed { index, example ->
            TextCard(text = example, color = colors[index])
        }
        guide.steps.forEach { StepBlock(it, colors) }

        HorizontalDivider(Modifier.padding(4.dp))
        CombinedSectionText(
            text = guide.outro,
            boldText = stringResource(R.string.remember),
            boldFirst = true,
            italic = true
        )
    }
}

@Composable
fun TextCard(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.LightGray
){
    Card(
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = modifier.fillMaxWidth()
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
        )
    }
}

@Composable
fun StepBlock(
    step: GuideContent.Step,
    exampleColors: List<Color>,
    modifier: Modifier = Modifier
){
    Column(modifier) {
        SectionTitle(step.name)
        Text(
            text = step.instruction,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        ExamplesBlock(step.examples, exampleColors)
    }
}

@Composable
fun ExamplesBlock(
    examples: List<GuideContent.ExampleForStep>,
    colors: List<Color>
) {
    examples.forEachIndexed { index, example ->
        ExpandableSectionCard(
            title = example.name,
            containerColor = colors[index],
            borderColor = Color.LightGray,
            elevation = 0.dp,
            expanded = false,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            example.points.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    if (example.points.size > 1) {
                        Text(
                            text = stringResource(R.string.point_symbol),
                            modifier = Modifier
                                .align(Alignment.Top)
                                .padding(end = 4.dp)
                        )
                    }
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuideBlockPreview() {
    CBTJournalTheme {
        GuideScreenContent(
            uiState = UiState.Success(InfoPreviewData.guide),
            canNavigateUp = true,
            onBack = {}
        )
    }
}
