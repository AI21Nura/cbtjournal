package com.ainsln.feature.distortions.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.ainsln.feature.distortions.adaptive.DistortionsListDetailScreen
import com.ainsln.feature.distortions.details.DistortionDetailsPlaceholder
import com.ainsln.feature.distortions.details.DistortionDetailsScreen
import kotlinx.serialization.Serializable

sealed interface DistortionsDestinations {
    @Serializable
    data object List : DistortionsDestinations

    @Serializable
    data class Detail(val id: Long? = null) : DistortionsDestinations

    @Serializable
    data object DetailPlaceholder : DistortionsDestinations

}


fun NavGraphBuilder.distortionsDestination() {
    composable<DistortionsDestinations.List> {
        DistortionsListDetailScreen()
    }
}

fun NavGraphBuilder.distortionDetailsDestination() {
    composable<DistortionsDestinations.Detail> {
        DistortionDetailsScreen()
    }
}

fun NavGraphBuilder.distortionDetailsPlaceholder() {
    composable<DistortionsDestinations.DetailPlaceholder> {
        DistortionDetailsPlaceholder()
    }
}

fun NavController.navigateToDistortionDetails(id: Long, navOptions: NavOptionsBuilder.() -> Unit = {}) {
    navigate(route = DistortionsDestinations.Detail(id), builder = navOptions)
}
