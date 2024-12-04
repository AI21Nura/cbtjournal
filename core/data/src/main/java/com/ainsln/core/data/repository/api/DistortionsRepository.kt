package com.ainsln.core.data.repository.api

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Distortion
import kotlinx.coroutines.flow.Flow

public interface DistortionsRepository {

    public fun getDistortions(): Flow<Result<List<Distortion>>>

    public fun getDistortionById(id: Long): Flow<Result<Distortion>>

    public fun getDistortionsByIds(ids: List<Long>): Flow<Result<List<Distortion>>>

}
