package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ainsln.core.database.model.TranslatedEmotion
import com.ainsln.core.database.model.TranslatedSelectedEmotion
import kotlinx.coroutines.flow.Flow

@Dao
public interface EmotionsDao {

    @Transaction
    @Query("SELECT e.id, t.name, e.color FROM Emotion AS e " +
            "JOIN EmotionTranslation AS t ON e.id = t.emotion_id " +
            "WHERE t.language_code = :langCode")
    public fun getAll(langCode: String): Flow<List<TranslatedEmotion>>

    @Transaction
    @Query(
        """
        SELECT * FROM SelectedEmotion se
        JOIN Emotion e ON se.emotion_id = e.id
        JOIN EmotionTranslation et ON e.id = et.emotion_id
        WHERE et.language_code = :langCode
    """
    )
    public fun getAllSelected(langCode: String): Flow<List<TranslatedSelectedEmotion>>

}
