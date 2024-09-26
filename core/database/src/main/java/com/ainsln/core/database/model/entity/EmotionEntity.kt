package com.ainsln.core.database.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Emotion")
public data class EmotionEntity(
    @PrimaryKey val id: Long,
    val color: Int
)
