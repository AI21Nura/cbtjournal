package com.ainsln.core.model

data class SelectedEmotion(
    val emotion: Emotion,
    val noteId: Long,
    val intensityBefore: Int,
    val intensityAfter: Int
)
