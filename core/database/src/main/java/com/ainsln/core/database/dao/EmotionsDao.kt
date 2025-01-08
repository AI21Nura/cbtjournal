package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ainsln.core.database.model.TranslatedEmotion
import com.ainsln.core.database.model.TranslatedSelectedEmotion
import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
public interface EmotionsDao {

    @Transaction
    @Query("""
        SELECT e.id, t.name, e.color FROM Emotion AS e 
        JOIN EmotionTranslation AS t ON e.id = t.emotion_id 
        WHERE t.language_code = :langCode
        AND (COALESCE(:ids, -1) = -1 OR e.id IN (:ids))
        """
    )
    public fun getEmotions(langCode: String, ids: List<Long>? = null): Flow<List<TranslatedEmotion>>

    @Transaction
    @Query(
        """
        SELECT * FROM SelectedEmotion se
        JOIN Emotion e ON se.emotion_id = e.id
        JOIN EmotionTranslation et ON e.id = et.emotion_id
        WHERE et.language_code = :langCode 
        AND (:noteId IS NULL OR se.note_id = :noteId)
    """
    )
    public fun getSelectedEmotions(langCode: String, noteId: Long? = null): Flow<List<TranslatedSelectedEmotion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insertEmotions(emotions: List<SelectedEmotionCrossRef>)

    @Delete
    public suspend fun deleteEmotions(emotions: List<SelectedEmotionCrossRef>)
}
