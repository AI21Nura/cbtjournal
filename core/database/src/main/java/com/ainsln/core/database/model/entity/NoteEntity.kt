package com.ainsln.core.database.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Note")
public data class NoteEntity(
    @PrimaryKey val id: Long,
    val date: Date,
    val situation: String,
    @ColumnInfo("body_reaction") val bodyReaction: String,
    @ColumnInfo("behavioral_reaction") val behavioralReaction: String,
    @ColumnInfo("distortions_ids") val distortionsIds: List<Long>

)
