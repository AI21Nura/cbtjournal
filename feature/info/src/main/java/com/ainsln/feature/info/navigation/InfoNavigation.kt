package com.ainsln.feature.info.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ainsln.feature.info.adaptive.InfoAdaptiveScreen
import kotlinx.serialization.Serializable

sealed interface InfoDestinations{
    @Serializable
    data object Main : InfoDestinations
}

fun NavGraphBuilder.mainInfoDestination(){
    composable<InfoDestinations.Main> {
        InfoAdaptiveScreen()
    }
}

fun NavController.navigateToHelp(navOptions: NavOptions){
    navigate(route = InfoDestinations.Main,  navOptions)
}
