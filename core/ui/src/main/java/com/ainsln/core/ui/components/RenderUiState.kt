package com.ainsln.core.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ainsln.core.ui.R
import com.ainsln.core.ui.components.appbar.DetailsAppBar
import com.ainsln.core.ui.state.UiState

@Composable
fun <T> RenderUiState(
    uiState: UiState<T>,
    @StringRes errMsgRes: Int,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (T) -> Unit
){
    when (uiState) {
        is UiState.Error -> {
            ErrorScreen(
                message = stringResource(
                    errMsgRes,
                    uiState.e.message ?: stringResource(R.string.unknown_error)
                ),
                contentPadding = contentPadding
            )
        }
        is UiState.Loading -> {
            LoadingScreen(contentPadding = contentPadding)
        }
        is UiState.Success -> {
            content(uiState.data)
        }
    }
}

@Composable
fun <T> RenderUiStateScaffold(
    uiState: UiState<T>,
    @StringRes topBarTitle: Int,
    @StringRes errMsgRes: Int,
    onBack: () -> Unit,
    canNavigateUp: Boolean,
    content: @Composable (T) -> Unit
){
    when (uiState) {
        is UiState.Error, is UiState.Loading -> {
            Scaffold(
                topBar = {
                    DetailsAppBar(
                        title = stringResource(topBarTitle),
                        onBack = onBack,
                        canNavigateUp = canNavigateUp
                    ) { }
                }
            ){ innerPadding ->
                if (uiState is UiState.Error){
                    ErrorScreen(
                        message = stringResource(
                            errMsgRes,
                            uiState.e.message ?: stringResource(R.string.unknown_error)
                        ),
                        contentPadding = innerPadding
                    )
                } else {
                    LoadingScreen(contentPadding = innerPadding)
                }
            }
        }
        is UiState.Success -> {
            content(uiState.data)
        }
    }
}
