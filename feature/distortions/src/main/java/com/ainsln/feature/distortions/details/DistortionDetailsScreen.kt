package com.ainsln.feature.distortions.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
internal fun DistortionDetailsScreen(
    viewModel: DistortionsDetailsViewModel = hiltViewModel()
) {
    val id by viewModel.id.collectAsState()
    Text("Not implemented yet. id = $id")
}

@Composable
internal fun DistortionDetailsContent(

){

}

@Composable
internal fun DistortionDetailsPlaceholder(){
    Text("Distortion Placeholder")
}
