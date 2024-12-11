package com.ainsln.feature.info.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ainsln.feature.info.guide.GuideScreen
import com.ainsln.feature.info.main.InfoScreen
import kotlinx.coroutines.launch

@Composable
internal fun InfoAdaptiveScreen(
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
){
    InfoAdaptiveContent(windowAdaptiveInfo)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun InfoAdaptiveContent(
    windowAdaptiveInfo: WindowAdaptiveInfo
){
    val coroutineScope = rememberCoroutineScope()
    val adaptiveNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo),
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.List),
        )
    )
    val paneExpansionState = rememberPaneExpansionState()
    paneExpansionState.setFirstPaneProportion(0.5f)

    val backHandler = { coroutineScope.launch { adaptiveNavigator.navigateBack() } }
    BackHandler(adaptiveNavigator.canNavigateBack()) { backHandler() }

    ListDetailPaneScaffold(
        directive = adaptiveNavigator.scaffoldDirective,
        value = adaptiveNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                InfoScreen(
                    openGuideScreen = {
                        coroutineScope.launch { adaptiveNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail) }
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                GuideScreen(
                    canNavigateUp = adaptiveNavigator.canNavigateBack(),
                    onBack = { backHandler() }
                )
            }
        },
        paneExpansionState = paneExpansionState
    )
}
