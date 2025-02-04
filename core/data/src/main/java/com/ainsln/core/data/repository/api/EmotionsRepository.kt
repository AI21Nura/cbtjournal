package com.ainsln.core.data.repository.api

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.Emotion
import kotlinx.coroutines.flow.Flow

public interface EmotionsRepository {

    public fun getAllEmotions(): Flow<Result<List<Emotion>>>

    public fun getEmotionsByIds(ids: List<Long>): Flow<Result<List<Emotion>>>

    public fun getAllSelectedEmotions(): Flow<Result<List<SelectedEmotion>>>

    public fun getSelectedByNoteId(noteId: Long): Flow<Result<List<SelectedEmotion>>>

    public suspend fun saveSelectedEmotions(emotions: List<SelectedEmotion>, noteId: Long)

    public suspend fun deleteSelectedEmotions(emotions: List<SelectedEmotion>, noteId: Long)

}
