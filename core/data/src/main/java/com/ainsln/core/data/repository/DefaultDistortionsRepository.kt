package com.ainsln.core.data.repository

import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.asFlowResult
import com.ainsln.core.data.result.map
import com.ainsln.core.data.util.ResourceManager
import com.ainsln.core.data.util.toDistortion
import com.ainsln.core.datastore.DistortionsDataSource
import com.ainsln.core.datastore.model.DistortionStore
import com.ainsln.core.model.Distortion
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


public class DefaultDistortionsRepository @Inject constructor(
    private val resourceManager: ResourceManager,
    private val dataSource: DistortionsDataSource<DistortionStore>
) : DistortionsRepository {

    override fun getDistortions(): Flow<Result<List<Distortion>>> {
        return dataSource.getAll().asFlowResult().map { result ->
            result.map { list -> list.map { it.toDistortion(resourceManager) } }
        }
    }

    override fun getDistortionById(id: Long): Flow<Result<Distortion>> {
        return dataSource.getById(id).asFlowResult().map { result ->
            result.map { it.toDistortion(resourceManager) }
        }
    }
}
