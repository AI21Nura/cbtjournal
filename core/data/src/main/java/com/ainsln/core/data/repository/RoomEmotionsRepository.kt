package com.ainsln.core.data.repository

import com.ainsln.core.data.repository.api.EmotionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.processFlowList
import com.ainsln.core.data.util.AppLocaleManager
import com.ainsln.core.data.util.toEmotion
import com.ainsln.core.data.util.toSelectedEmotion
import com.ainsln.core.data.util.toSelectedEmotionCrossRef
import com.ainsln.core.database.dao.EmotionsDao
import com.ainsln.core.model.Emotion
import com.ainsln.core.model.SelectedEmotion
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

public class RoomEmotionsRepository @Inject constructor(
    private val emotionsDao: EmotionsDao,
    private val localeManager: AppLocaleManager
) : EmotionsRepository {

    private val langCode get() = localeManager.getLocale().code

    override fun getAllEmotions(): Flow<Result<List<Emotion>>> {
        return processFlowList(emotionsDao.getEmotions(langCode)) { it.toEmotion() }
    }

    override fun getAllSelectedEmotions(): Flow<Result<List<SelectedEmotion>>> {
        return processFlowList(emotionsDao.getSelectedEmotions(langCode)) { it.toSelectedEmotion() }
    }

    override fun getEmotionsByIds(ids: List<Long>): Flow<Result<List<Emotion>>> {
        return processFlowList(emotionsDao.getEmotions(langCode, ids)) { it.toEmotion() }
    }

    override fun getSelectedByNoteId(noteId: Long): Flow<Result<List<SelectedEmotion>>> {
        return processFlowList(emotionsDao.getSelectedEmotions(langCode, noteId)) {
            it.toSelectedEmotion()
        }
    }

    override suspend fun saveSelectedEmotions(emotions: List<SelectedEmotion>, noteId: Long) {
        emotionsDao.insertEmotions(emotions.map { it.toSelectedEmotionCrossRef(noteId) })
    }

    override suspend fun deleteSelectedEmotions(emotions: List<SelectedEmotion>, noteId: Long) {
        emotionsDao.deleteEmotions(emotions.map { it.toSelectedEmotionCrossRef(noteId) })
    }
}
