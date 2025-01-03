package com.ainsln.feature.distortions.adaptive

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ainsln.feature.distortions.list.DistortionsScreen
import com.ainsln.feature.distortions.navigation.DistortionsDestinations
import com.ainsln.feature.distortions.navigation.distortionDetailsDestination
import com.ainsln.feature.distortions.navigation.distortionDetailsPlaceholder
import com.ainsln.feature.distortions.navigation.navigateToDistortionDetails
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data object DistortionDetailPaneNavHost

@Composable
internal fun DistortionsListDetailScreen(
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    DistortionsListDetailContent(windowAdaptiveInfo)
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun DistortionsListDetailContent(
    windowAdaptiveInfo: WindowAdaptiveInfo
) {
    val coroutineScope = rememberCoroutineScope()
    val listDetailNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo),
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem<Nothing>(ListDetailPaneScaffoldRole.List),
        )
    )

    val paneExpansionState = rememberPaneExpansionState()
    paneExpansionState.setFirstPaneProportion(0.4f)

    var nestedNavHostStartDestination: DistortionsDestinations by remember {
        mutableStateOf(DistortionsDestinations.DetailPlaceholder)
    }

    val backHandler = {
        coroutineScope.launch {
            listDetailNavigator.navigateBack()
            nestedNavHostStartDestination = DistortionsDestinations.DetailPlaceholder
        }
    }

    BackHandler(listDetailNavigator.canNavigateBack()) { backHandler() }

    var nestedNavKey by rememberSaveable(
        stateSaver = Saver({ it.toString() }, UUID::fromString)
    ) {
        mutableStateOf(UUID.randomUUID())
    }

    val nestedNavController = key(nestedNavKey) {
        rememberNavController()
    }

    fun onDistortionClickDetailPane(distortionId: Long) {
        coroutineScope.launch {
            if (listDetailNavigator.isDetailPaneVisible()) {
                nestedNavController.navigateToDistortionDetails(distortionId) {
                    popUpTo(DistortionDetailPaneNavHost)
                }
            } else {
                nestedNavKey = UUID.randomUUID()
                nestedNavHostStartDestination = DistortionsDestinations.Detail(distortionId)
            }
            listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    val listState = rememberLazyListState()

    ListDetailPaneScaffold(
        directive = listDetailNavigator.scaffoldDirective,
        value = listDetailNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                DistortionsScreen(
                    onDistortionClick = ::onDistortionClickDetailPane,
                    listState = listState
                )
            }
        },
        detailPane = {
            AnimatedPane {
                NavHost(
                    navController = nestedNavController,
                    startDestination = nestedNavHostStartDestination,
                    route = DistortionDetailPaneNavHost::class
                ) {
                    distortionDetailsDestination(
                        canNavigateBack = listDetailNavigator.canNavigateBack(),
                        onBack = { backHandler() }
                    )
                    distortionDetailsPlaceholder()
                }
            }
        },
        paneExpansionState = paneExpansionState
    )

}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun <T> ThreePaneScaffoldNavigator<T>.isDetailPaneVisible(): Boolean =
    scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
