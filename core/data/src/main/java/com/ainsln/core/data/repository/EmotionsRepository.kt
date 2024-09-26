package com.ainsln.core.data.repository

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.Emotion
import kotlinx.coroutines.flow.Flow

public interface EmotionsRepository {

    public fun getAllEmotions(langCode: String = "en"): Flow<Result<List<Emotion>>>

    public fun getAllSelectedEmotions(langCode: String = "en"): Flow<Result<List<SelectedEmotion>>>
}
