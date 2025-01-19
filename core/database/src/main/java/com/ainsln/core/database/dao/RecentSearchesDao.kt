package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.ainsln.core.database.model.entity.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
public interface RecentSearchesDao {

    @Query("SELECT * FROM RecentSearch ORDER BY queried_date DESC LIMIT :limit")
    public fun get(limit: Int): Flow<List<RecentSearchEntity>>

    @Upsert
    public suspend fun upsert(search: RecentSearchEntity)

    @Delete
    public suspend fun delete(search: RecentSearchEntity)

    @Query("DELETE FROM RecentSearch")
    public suspend fun clear()

}
