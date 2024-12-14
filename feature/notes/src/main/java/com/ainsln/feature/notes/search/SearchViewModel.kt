package com.ainsln.feature.notes.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.api.RecentSearchesRepository
import com.ainsln.core.domain.SearchNotesUseCase
import com.ainsln.core.model.RecentSearch
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.notes.state.RecentSearchUiState
import com.ainsln.feature.notes.state.SearchResultUiState
import com.ainsln.feature.notes.state.toSearchState
import com.ainsln.feature.notes.state.toState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    searchNotesUseCase: SearchNotesUseCase,
    private val recentSearchesRepository: RecentSearchesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val searchQuery = savedStateHandle.getStateFlow(SEARCH_QUERY, "")

    val recentSearches: StateFlow<RecentSearchUiState> = recentSearchesRepository.getRecentSearches()
        .map { it.toState() }
        .toStateFlow(UiState.Loading)


    @OptIn(FlowPreview::class)
    val searchResults: StateFlow<SearchResultUiState> = searchQuery
        .debounce(250)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank())
                flowOf(SearchResultUiState.EmptyResult)
            else
                searchNotesUseCase(query).map { it.toSearchState() }
        }
        .toStateFlow(SearchResultUiState.EmptyResult)

    fun changeSearchQuery(query: String){
        savedStateHandle[SEARCH_QUERY] = query
    }

    fun deleteRecentSearch(recentSearch: RecentSearch){
        viewModelScope.launch {
            recentSearchesRepository.deleteRecentSearch(recentSearch)
        }
    }

    fun saveRecentSearch(query: String){
        viewModelScope.launch {
            recentSearchesRepository.upsertRecentSearch(
                RecentSearch(query, Date(System.currentTimeMillis()))
            )
        }
    }

    fun clearRecentSearchesHistory(){
        viewModelScope.launch { recentSearchesRepository.clearAll() }
    }

    private fun <T> Flow<T>.toStateFlow(initState: T): StateFlow<T> =
        stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initState
        )

    companion object {
        private const val SEARCH_QUERY = "searchQuery"
    }
}
