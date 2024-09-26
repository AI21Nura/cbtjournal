package com.ainsln.core.model

import java.util.Date

data class ShortNote(
    val id: Long,
    val date: Date,
    val situation: String,
    val emotions: List<SelectedEmotion>
)
