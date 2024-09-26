package com.ainsln.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.ThoughtEntity

public data class NoteWithThoughts(
    @Embedded val note: NoteEntity,

    @Relation(
        entityColumn = "note_id",
        parentColumn = "id"
    )
    val thoughts: List<ThoughtEntity>
)
