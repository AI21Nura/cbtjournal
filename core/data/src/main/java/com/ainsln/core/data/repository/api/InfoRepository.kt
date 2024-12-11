package com.ainsln.core.data.repository.api

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.GuideContent
import com.ainsln.core.model.InfoContent
import kotlinx.coroutines.flow.Flow

public interface InfoRepository {
    public fun getInfoContent(): Flow<Result<InfoContent>>
    public fun getGuide(): Flow<Result<GuideContent>>
}
