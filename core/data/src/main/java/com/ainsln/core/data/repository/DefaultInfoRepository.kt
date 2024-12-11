package com.ainsln.core.data.repository

import com.ainsln.core.data.repository.api.InfoRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.data.util.AppLocaleManager
import com.ainsln.core.data.util.toGuideContent
import com.ainsln.core.data.util.toInfoContent
import com.ainsln.core.datastore.info.ContentReader
import com.ainsln.core.datastore.info.model.GuideData
import com.ainsln.core.datastore.info.model.InfoData
import com.ainsln.core.model.GuideContent
import com.ainsln.core.model.InfoContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class DefaultInfoRepository @Inject constructor(
    private val contentReader: ContentReader<InfoData, GuideData>,
    private val localeManager: AppLocaleManager
) : InfoRepository {

    private val langCode get() = localeManager.getLocale().code

    override fun getInfoContent(): Flow<Result<InfoContent>> = createFlow {
        contentReader.readInfo(langCode).toInfoContent()
    }

    override fun getGuide(): Flow<Result<GuideContent>> = createFlow {
        contentReader.readGuide(langCode).toGuideContent()
    }

    private fun <T> createFlow(block: () -> T) = flow<Result<T>> {
        emit(Result.Success(block()))
    }
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(e)) }

}

