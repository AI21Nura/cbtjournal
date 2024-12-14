package com.ainsln.core.database.model.entity

import androidx.room.Entity
import androidx.room.Fts4

@Entity(tableName = "NoteFts")
@Fts4(contentEntity = NoteEntity::class)
public data class NoteFtsEntity(
    val id: String,
    val situation: String
)

