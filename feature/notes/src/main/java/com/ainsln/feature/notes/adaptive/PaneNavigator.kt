package com.ainsln.feature.notes.adaptive

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
class PaneNavigator(
    private val coroutineScope: CoroutineScope,
    private val listDetailNavigator: ThreePaneScaffoldNavigator<Nothing>
) {
    val canNavigateBack get() = listDetailNavigator.canNavigateBack()
    val scaffoldDirective get() = listDetailNavigator.scaffoldDirective
    val scaffoldValue get() = listDetailNavigator.scaffoldValue

    fun areBothPanesVisible(): Boolean =
        scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded
                && scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    fun navigateBack(){
        coroutineScope.launch { listDetailNavigator.navigateBack() }
    }

    fun navigateToDetailsPane(){
        coroutineScope.launch { listDetailNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail) }
    }
}
