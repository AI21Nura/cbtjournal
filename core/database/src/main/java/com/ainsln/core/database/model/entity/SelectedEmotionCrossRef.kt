package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "SelectedEmotion",
    primaryKeys = ["note_id", "emotion_id"],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EmotionEntity::class,
            parentColumns = ["id"],
            childColumns = ["emotion_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["note_id"]),
        Index(value = ["emotion_id"])
    ]
)
public data class SelectedEmotionCrossRef(
    @ColumnInfo("note_id") val noteId: Long,
    @ColumnInfo("emotion_id") val emotionId: Long,
    @ColumnInfo("intensity_before") val intensityBefore: Int,
    @ColumnInfo("intensity_after") val intensityAfter: Int
)
