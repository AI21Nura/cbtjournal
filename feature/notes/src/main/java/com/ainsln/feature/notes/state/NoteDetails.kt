package com.ainsln.feature.notes.state

import android.graphics.Color
import androidx.compose.runtime.mutableStateListOf
import com.ainsln.core.model.Distortion
import com.ainsln.core.model.Emotion
import com.ainsln.core.model.Note
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.Thought
import com.ainsln.feature.notes.R
import java.util.Date

data class NoteDetails(
    val id: Long = 0,
    val date: Date = Date(System.currentTimeMillis()),
    val situation: String = "",
    val bodyReaction: String = "test body",
    val behavioralReaction: String = "test behav",
    val thoughts: MutableList<Thought> = mutableStateListOf(),
    val emotions: MutableList<SelectedEmotion> = mutableStateListOf(
        SelectedEmotion(
            emotion = Emotion(id = 1, name = "Joy", color = Color.MAGENTA),
            noteId = 0,
            intensityBefore = 15,
            intensityAfter = 100
        )
    ),
    val distortions: List<Distortion> = listOf(
        Distortion(
            id = 1,
            name = "Labeling",
            shortDescription = "",
            longDescription = "",
            examples = emptyList(),
            iconResId = R.drawable.ic_sample
        )
    )
) {
    fun checkRequiredFields(): String {
        var result = ""
        if (situation == "") result += "\n• Situation"
        if (emotions.isEmpty()) result += "\n• Emotions"
        return result
    }
}

fun NoteDetails.toNote(id: Long? = null) = Note(
    id = id ?: 0,
    date = date,
    situation = situation,
    bodyReaction = bodyReaction,
    behavioralReaction = behavioralReaction,
    thoughts = thoughts,
    emotions = emotions,
    distortionsIds = distortions.map { it.id },
    distortions = distortions
)

fun Note.toNoteDetails() = NoteDetails(
    id = id,
    date = date,
    situation = situation,
    bodyReaction = bodyReaction,
    behavioralReaction = behavioralReaction,
    thoughts = mutableStateListOf(*thoughts.toTypedArray()),
    emotions = mutableStateListOf(*emotions.toTypedArray()),
    distortions = distortions
)
