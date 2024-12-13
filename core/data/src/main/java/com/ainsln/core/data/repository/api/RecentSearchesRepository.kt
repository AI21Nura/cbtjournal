package com.ainsln.core.data.repository.api

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.RecentSearch
import kotlinx.coroutines.flow.Flow

public interface RecentSearchesRepository {
    public fun getRecentSearches(limit: Int = 7): Flow<Result<List<RecentSearch>>>

    public suspend fun upsertRecentSearch(search: RecentSearch)

    public suspend fun deleteRecentSearch(search: RecentSearch)

    public suspend fun clearAll()
}
