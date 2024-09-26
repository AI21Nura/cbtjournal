package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "EmotionTranslation",
    primaryKeys = ["emotion_id", "language_code"],
    foreignKeys = [ForeignKey(
        entity = EmotionEntity::class,
        parentColumns = ["id"],
        childColumns = ["emotion_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["emotion_id"])]
)
public data class EmotionTranslationEntity(
    @ColumnInfo("emotion_id") val emotionId: Long,
    @ColumnInfo("language_code") val languageCode: String,
    val name: String
)
