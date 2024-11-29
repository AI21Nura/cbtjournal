package com.ainsln.core.data.util

import com.ainsln.core.database.model.NoteWithThoughts
import com.ainsln.core.database.model.TranslatedEmotion
import com.ainsln.core.database.model.TranslatedSelectedEmotion
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import com.ainsln.core.database.model.entity.ThoughtEntity
import com.ainsln.core.datastore.model.DistortionStore
import com.ainsln.core.model.Distortion
import com.ainsln.core.model.Emotion
import com.ainsln.core.model.Note
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.ShortNote
import com.ainsln.core.model.Thought

internal fun DistortionStore.toDistortion(resourceManager: ResourceManager): Distortion = Distortion(
    id = id,
    name = resourceManager.getString(name),
    shortDescription = resourceManager.getString(shortDescription),
    longDescription = resourceManager.getString(longDescription),
    examples = resourceManager.getStringArray(examples).toList(),
    iconResId = iconResId
)

internal fun NoteEntity.toShortNote(): ShortNote =
    ShortNote(
        id = id,
        date = date,
        situation = situation,
        emotions = emptyList()
    )

internal fun NoteWithThoughts.toNote(): Note =
    Note(
        id = note.id,
        date = note.date,
        situation = note.situation,
        bodyReaction = note.bodyReaction,
        behavioralReaction = note.behavioralReaction,
        thoughts = thoughts.map { it.toThought() },
        emotions = emptyList(),
        distortionsIds = note.distortionsIds,
        distortions = emptyList()
    )

internal fun ThoughtEntity.toThought(): Thought =
    Thought(
        id = id,
        text = text,
        alternativeThought = alternativeThought
    )

internal fun TranslatedEmotion.toEmotion(): Emotion =
    Emotion(
        id = id,
        name = name,
        color = color
    )

internal fun TranslatedSelectedEmotion.toSelectedEmotion(): SelectedEmotion =
    SelectedEmotion(
        emotion = emotion.toEmotion(),
        noteId = selection.noteId,
        intensityAfter = selection.intensityAfter,
        intensityBefore = selection.intensityBefore
    )

internal fun Note.toNoteEntity(): NoteEntity =
    NoteEntity(
        id = id,
        date = date,
        situation = situation,
        bodyReaction = bodyReaction,
        behavioralReaction = behavioralReaction,
        distortionsIds = distortionsIds
    )

internal fun Thought.toThoughtEntity(noteId: Long): ThoughtEntity =
    ThoughtEntity(
        id = id,
        noteId = noteId,
        text = text,
        alternativeThought = alternativeThought
    )

internal fun SelectedEmotion.toSelectedEmotionCrossRef(noteId: Long): SelectedEmotionCrossRef =
    SelectedEmotionCrossRef(
        noteId = noteId,
        emotionId = emotion.id,
        intensityBefore = intensityBefore,
        intensityAfter = intensityAfter
    )
