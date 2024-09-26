package com.ainsln.core.model

import java.util.Date

data class Note(
    val id: Long,
    val date: Date,
    val situation: String,
    val bodyReaction: String,
    val behavioralReaction: String,
    val thoughts: List<Thought>,
    val emotions: List<SelectedEmotion>,
    val distortionsIds: List<Long>,
    val distortions: List<Distortion>
)

data class Thought(
    val id: Long,
    val text: String,
    val alternativeThought: String
)
