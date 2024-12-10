package com.ainsln.core.ui.components.text

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ainsln.core.ui.R
import com.ainsln.core.ui.theme.CBTJournalTheme

@Composable
fun SectionCard(
    title: String,
    modifier: Modifier = Modifier,
    trailingIcon: @Composable () -> Unit,
    content: @Composable (Modifier) -> Unit,
){
    OutlinedCard(
        //border = BorderStroke(width = 1.dp, color = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 12.dp)
            ) {
                SectionTitle(title)
                Spacer(Modifier.weight(1f))
                trailingIcon()
            }

            content(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            )
        }
    }
}

@Composable
fun ExpandableSectionCard(
    title: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    borderColor: Color = Color.White,
    containerColor: Color? = null,
    elevation: Dp = 4.dp,
    content: @Composable (Modifier) -> Unit,
) {
    if (containerColor != null)
        ExpandableSectionFilledCard(
            containerColor = containerColor,
            borderColor = borderColor,
            elevation = elevation,
            modifier = modifier
        ) { ExpandableCardContent(title, expanded, content) }
    else
        ExpandableSectionOutlinedCard(
            borderColor = borderColor,
            elevation = elevation,
            modifier = modifier
        ) { ExpandableCardContent(title, expanded, content) }
}

@Composable
private fun ExpandableSectionFilledCard(
    containerColor: Color,
    elevation: Dp,
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(width = 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(elevation),
        modifier = modifier
    ) { content() }
}

@Composable
private fun ExpandableSectionOutlinedCard(
    elevation: Dp,
    borderColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    OutlinedCard(
        //border = BorderStroke(width = 1.dp, color = borderColor),
        elevation = CardDefaults.cardElevation(elevation),
        modifier = modifier
    ) { content() }
}

@Composable
private fun ExpandableCardContent(
    title: String,
    expanded: Boolean = true,
    content: @Composable (Modifier) -> Unit,
){
    var isExpanded by rememberSaveable { mutableStateOf(expanded) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 4.dp, horizontal = 12.dp)
        ) {
            SectionTitle(title = title, modifier = Modifier.weight(1f))
            ExpandButton(
                isExpanded = isExpanded,
                onClick = { isExpanded = !isExpanded }
            )
        }
        if (isExpanded) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp,
                        start = 12.dp,
                        end = 12.dp
                    )
            ) {
                content(Modifier)
            }
        }
    }
}

@Composable
fun ExpandButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.toggle_visibility)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableSectionCardPreview() {
    var text by remember { mutableStateOf("") }
    CBTJournalTheme {

        ExpandableSectionCard(
            title = "Situation",
        ) { modifier ->
            InputTextField(
                subtitle = "Describe situation that caused you negative emotions",
                placeholder = "Enter text...",
                text = text,
                onTextChanged = { text = it },
                modifier = modifier
            )
        }


    }
}
