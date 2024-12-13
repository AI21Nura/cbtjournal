package com.ainsln.feature.notes.search

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ainsln.core.model.RecentSearch
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.CBTJournalTheme
import com.ainsln.data.NotesPreviewData
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.list.NotesContent
import com.ainsln.feature.notes.state.RecentSearchUiState
import com.ainsln.feature.notes.state.SearchResultUiState
import java.util.Date

@Composable
fun NotesSearchScreen(
    onNoteClick: (Long) -> Unit,
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val recentSearches by viewModel.recentSearches.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreenContent(
        searchQuery = searchQuery,
        searchResults = searchResults,
        recentSearches = recentSearches,
        onNoteClick = {
            keyboardController?.hide()
            onNoteClick(it)
        },
        onClearSearchesClick = viewModel::clearRecentSearchesHistory,
        onDeleteRecentSearchClick = viewModel::deleteRecentSearch,
        onSearchClick = {
            keyboardController?.hide()
            viewModel.saveRecentSearch(it)
        },
        onQueryChange = viewModel::changeSearchQuery,
        onBack = {
            viewModel.changeSearchQuery("")
            onBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    searchQuery: String,
    searchResults: SearchResultUiState,
    recentSearches: RecentSearchUiState,
    onNoteClick: (Long) -> Unit,
    onClearSearchesClick: () -> Unit,
    onDeleteRecentSearchClick: (RecentSearch) -> Unit,
    onSearchClick: (String) -> Unit,
    onQueryChange: (String) -> Unit,
    onBack: () -> Unit
){
    Scaffold(
        topBar = {
            SearchBar(
                inputField = {
                    SearchTextField(
                        searchQuery = searchQuery,
                        onQueryChange = onQueryChange,
                        onSearchClick = onSearchClick,
                        onBack = onBack
                    )
                },
                expanded = searchQuery.isBlank(),
                onExpandedChange = { },
                colors = SearchBarDefaults.colors(
                    containerColor = Color.Transparent,
                    dividerColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()

            ) {
                RecentSearchesContent(
                    recentSearches = recentSearches,
                    onRecentSearchClick = { query ->
                        onQueryChange(query)
                        onSearchClick(query)
                    },
                    onClearSearchesClick = onClearSearchesClick,
                    onDeleteRecentSearchClick = onDeleteRecentSearchClick
                )
            }
        }
    ) { innerPadding ->
        BackHandler {
            Log.d("TAG", "BackHandler2")

            onBack()
        }
        Column(
            Modifier.fillMaxSize().padding(innerPadding)){
            SearchResultsContent(
                searchResults = searchResults,
                onNoteClick = onNoteClick
            )
        }
    }

}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        placeholder = { Text(stringResource(R.string.search_placeholder)) },
        leadingIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        trailingIcon = {
            if (searchQuery.isNotBlank()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.clear_query)
                    )
                }
            }
        },
        onValueChange = { onQueryChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Enter) {
                    onSearchClick(searchQuery)
                    true
                } else {
                    false
                }
            },
        shape = RoundedCornerShape(16.dp),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchClick(searchQuery) },
        ),
        maxLines = 1,
        singleLine = true,
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun RecentSearchesContent(
    recentSearches: RecentSearchUiState,
    onRecentSearchClick: (String) -> Unit,
    onClearSearchesClick: () -> Unit,
    onDeleteRecentSearchClick: (RecentSearch) -> Unit
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when(recentSearches){
            is UiState.Success -> {
                if (recentSearches.data.isNotEmpty())
                    RecentSearchesBody(
                        recentSearches = recentSearches.data,
                        onRecentSearchClick = onRecentSearchClick,
                        onClearSearchesClick = onClearSearchesClick,
                        onDeleteRecentSearchClick = onDeleteRecentSearchClick
                    )
                else
                    EmptyScreen(R.string.no_recent_searches)
            }
            is UiState.Loading -> LoadingScreen()
            is UiState.Error -> EmptyScreen(R.string.no_recent_searches)
        }
    }
}


@Composable
fun RecentSearchesBody(
    recentSearches: List<RecentSearch>,
    onRecentSearchClick: (String) -> Unit,
    onClearSearchesClick: () -> Unit,
    onDeleteRecentSearchClick: (RecentSearch) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.recent_searches),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyColumn {
            items(items = recentSearches){ item ->
                RecentSearchItem(
                    recentSearch = item,
                    onItemClick = onRecentSearchClick,
                    onDeleteClick = onDeleteRecentSearchClick
                )
            }
        }

        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = { onClearSearchesClick() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.DarkGray
                )
            ) {
                Text(
                    text = stringResource(R.string.clear_all),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun RecentSearchItem(
    recentSearch: RecentSearch,
    onItemClick: (String) -> Unit,
    onDeleteClick: (RecentSearch) -> Unit
){
    Box(
        modifier = Modifier.clickable { onItemClick(recentSearch.query) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = recentSearch.query,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onDeleteClick(recentSearch) }, modifier = Modifier.size(20.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.delete_recent_search),
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }

        }
    }

}

@Composable
fun SearchResultsContent(
    searchResults: SearchResultUiState,
    onNoteClick: (Long) -> Unit
) {
    when (searchResults) {
        is SearchResultUiState.EmptyResult -> EmptyScreen(R.string.no_search_results)
        is SearchResultUiState.Loading -> LoadingScreen()
        is SearchResultUiState.Success -> {
            NotesContent(
                notes = searchResults.notes,
                onNoteClick = onNoteClick,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
        is SearchResultUiState.LoadFailed -> ErrorScreen(
            searchResults.e.message ?: stringResource(R.string.no_search_results)
        )
    }
}

@Composable
fun EmptyScreen(
    msgResId: Int,
    modifier: Modifier = Modifier
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(msgResId))
    }
}

@Preview(showBackground = true)
@Composable
fun SearchResultsContentPreview(){
    CBTJournalTheme {
        SearchScreenContent(
            searchQuery = "during",
            recentSearches = UiState.Success(emptyList()),
            searchResults = SearchResultUiState.Success(NotesPreviewData.shortNotes),
            onQueryChange = {},
            onClearSearchesClick = {},
            onNoteClick = {},
            onDeleteRecentSearchClick = {},
            onBack = {},
            onSearchClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecentSearchesScreenPreview(){
    CBTJournalTheme {
        SearchScreenContent(
            searchQuery = "",
            recentSearches = UiState.Success(data = listOf(
                RecentSearch(query = "Query 1",  Date(1734036063)),
                RecentSearch(query = "Query Long", Date(1734067415)),
                RecentSearch(query = "Query 3", Date(1734067415)),
            )),
            searchResults = SearchResultUiState.Success(emptyList()),
            onQueryChange = {},
            onClearSearchesClick = {},
            onNoteClick = {},
            onDeleteRecentSearchClick = {},
            onBack = {},
            onSearchClick = {}
        )
    }
}
