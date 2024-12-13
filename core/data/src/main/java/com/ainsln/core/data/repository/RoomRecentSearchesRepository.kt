package com.ainsln.core.data.repository

import com.ainsln.core.data.repository.api.RecentSearchesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.processFlowList
import com.ainsln.core.data.util.toRecentSearch
import com.ainsln.core.data.util.toRecentSearchEntity
import com.ainsln.core.database.dao.RecentSearchesDao
import com.ainsln.core.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

public class RoomRecentSearchesRepository @Inject constructor(
    private val recentSearchesDao: RecentSearchesDao
) : RecentSearchesRepository {

    override fun getRecentSearches(limit: Int): Flow<Result<List<RecentSearch>>> {
        return processFlowList(recentSearchesDao.get(limit)){ it.toRecentSearch() }
    }

    override suspend fun upsertRecentSearch(search: RecentSearch) {
        recentSearchesDao.insertOrReplace(search.toRecentSearchEntity())
    }

    override suspend fun deleteRecentSearch(search: RecentSearch) {
        recentSearchesDao.delete(search.toRecentSearchEntity())
    }

    override suspend fun clearAll() {
        recentSearchesDao.clear()
    }
}
