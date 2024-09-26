package com.ainsln.core.database.model

import androidx.room.Embedded
import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef

public data class TranslatedEmotion(
    val id: Long,
    val name: String,
    val color: Int
)

public data class TranslatedSelectedEmotion(
    @Embedded val emotion: TranslatedEmotion,
    @Embedded val selection: SelectedEmotionCrossRef
)
