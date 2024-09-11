package com.ainsln.core.datastore

import kotlinx.coroutines.flow.Flow

public interface DistortionsDataSource<T> {

    public fun getAll(): Flow<List<T>>

    public fun getById(id: Long): Flow<T>

}
