package com.ainsln.feature.info.main

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.InfoContent
import com.ainsln.core.ui.components.RenderUiState
import com.ainsln.core.ui.components.appbar.TopDestinationAppBar
import com.ainsln.core.ui.components.text.ClickableText
import com.ainsln.core.ui.components.text.ExpandableSectionCard
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.InfoPreviewData
import com.ainsln.feature.info.R
import com.ainsln.feature.info.state.InfoUiState

@Composable
fun InfoScreen(
    openGuideScreen: () -> Unit,
    viewModel: InfoViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    InfoScreenContent(uiState, openGuideScreen, viewModel::sendFeedback)
}

@Composable
fun InfoScreenContent(
    uiState: InfoUiState,
    openGuideScreen: () -> Unit,
    sendFeedback: (Context, String) -> Unit
) {
    Scaffold(
        topBar = {
            TopDestinationAppBar(
                title = stringResource(R.string.main_title),
                alignCenter = true
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            RenderUiState(
                uiState = uiState,
                errMsgRes = R.string.help_error
            ) { data ->
                InformationBlock(data, openGuideScreen, sendFeedback)
            }
        }
    }
}

@Composable
fun InformationBlock(
    info: InfoContent,
    openGuideScreen: () -> Unit,
    sendFeedback: (Context, String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        start = 16.dp, end = 16.dp, bottom = 16.dp
    )
) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = info.intro, textAlign = TextAlign.Justify)
        Column {
            Text(text = info.guide, textAlign = TextAlign.Justify)
            ClickableText(
                text = "",
                clickableText = stringResource(R.string.open_guide),
                onTextClick = { openGuideScreen() },
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        FaqSection(
            questions = info.faq,
            backgroundColor = colorResource(R.color.cornflower_crayola).copy(alpha = 0.3f)
        )
        ClickableText(
            text = info.feedback,
            clickableText = info.feedbackEmail,
            onTextClick = { sendFeedback(context, info.feedbackEmail) },
            linkTag = "FeedbackMail"
        )
    }
}

@Composable
fun FaqSection(
    questions: List<InfoContent.Question>,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = Color.Gray
){
    Column(modifier.padding(vertical = 4.dp)) {
        Text(
            text = "FAQ",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(4.dp)
        )
        questions.forEach { question ->
            ExpandableSectionCard(
                title = question.text,
                expanded = false,
                borderColor = borderColor,
                containerColor = backgroundColor,
                elevation = 0.dp,
                modifier = Modifier.padding(vertical = 4.dp)
            ){
                Text(
                    text = question.answer,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun InformationBlockPreview(){
    CBTJournalTheme {
        InfoScreenContent(
            uiState = UiState.Success(InfoPreviewData.InfoContent),
            openGuideScreen = {},
            sendFeedback = {_, _ ->}
        )
    }
}
