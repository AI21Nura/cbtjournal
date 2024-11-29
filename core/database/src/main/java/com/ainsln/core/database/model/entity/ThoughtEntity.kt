package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Thought",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["id"],
        childColumns = ["note_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["note_id"])]
)
public data class ThoughtEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo("note_id") val noteId: Long,
    @ColumnInfo("thought_text") val text: String,
    @ColumnInfo("alternative_thought") val alternativeThought: String
)
