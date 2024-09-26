package com.ainsln.core.datastore.distortion

import kotlinx.coroutines.flow.Flow

public interface DistortionsDataSource<T> {

    public fun getAll(): Flow<List<T>>

    public fun getById(id: Long): Flow<T>

    public fun getByIds(ids: List<Long>): Flow<List<T>>

}
