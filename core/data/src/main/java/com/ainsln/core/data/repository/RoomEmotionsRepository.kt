package com.ainsln.core.data.repository

import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.asFlowResult
import com.ainsln.core.data.result.map
import com.ainsln.core.data.util.toEmotion
import com.ainsln.core.data.util.toSelectedEmotion
import com.ainsln.core.database.dao.EmotionsDao
import com.ainsln.core.model.Emotion
import com.ainsln.core.model.SelectedEmotion
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

public class RoomEmotionsRepository @Inject constructor(
    private val emotionsDao: EmotionsDao
) :EmotionsRepository {

    override fun getAllEmotions(langCode: String): Flow<Result<List<Emotion>>> {
        return emotionsDao.getAll(langCode).asFlowResult().map { result ->
            result.map { list -> list.map { it.toEmotion() } }
        }
    }

    override fun getAllSelectedEmotions(langCode: String): Flow<Result<List<SelectedEmotion>>> {
        return emotionsDao.getAllSelected(langCode).asFlowResult().map { result ->
            result.map { list -> list.map { it.toSelectedEmotion() } }
        }
    }
}
