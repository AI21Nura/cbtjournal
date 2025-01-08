package com.ainsln.cbtjournal.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.ainsln.feature.distortions.dialog.MultiSelectionDialog
import com.ainsln.feature.distortions.navigation.distortionsDestination
import com.ainsln.feature.distortions.navigation.navigateToDistortions
import com.ainsln.feature.info.navigation.mainInfoDestination
import com.ainsln.feature.info.navigation.navigateToHelp
import com.ainsln.feature.notes.navigation.NotesDestinations
import com.ainsln.feature.notes.navigation.navigateToJournal
import com.ainsln.feature.notes.navigation.notesDestination
import kotlin.reflect.KClass

@Composable
fun AppNavHost(){

    val navController = rememberNavController()
    val currentDestination: NavDestination? = navController.currentBackStackEntryAsState().value?.destination
    val nestedNotesController = rememberNavController()

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TopLevelDestination.entries.forEach { destination ->
                item(
                    selected = currentDestination.isRouteInHierarchy(destination.route),
                    onClick = { navController.navigateToTopLevelDestination(destination) },
                    label = { Text(stringResource(destination.titleResId)) },
                    icon = {
                        Icon(
                            painter = painterResource(destination.iconResId),
                            contentDescription = stringResource(destination.titleResId)
                        )
                    },
                    modifier = Modifier.testTag(destination.tag)
                )
            }
        }
    ){
        NavHost(navController = navController, startDestination = NotesDestinations.List){
            mainInfoDestination()

            distortionsDestination()

            notesDestination(nestedNotesController) { args ->
                MultiSelectionDialog(args)
            }
        }
    }
}

fun NavController.navigateToTopLevelDestination(destination: TopLevelDestination){
    val navOptions = navOptions {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    when(destination){
        TopLevelDestination.JOURNAL -> navigateToJournal(navOptions)
        TopLevelDestination.DISTORTIONS -> navigateToDistortions(navOptions)
        TopLevelDestination.HELP -> navigateToHelp(navOptions)
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>) =
    this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
