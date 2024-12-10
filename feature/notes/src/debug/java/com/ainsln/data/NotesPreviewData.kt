package com.ainsln.data

import com.ainsln.core.model.Distortion
import com.ainsln.core.model.Emotion
import com.ainsln.core.model.Note
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.ShortNote
import com.ainsln.core.model.Thought
import com.ainsln.feature.notes.R
import com.ainsln.feature.notes.editor.NoteModifier
import com.ainsln.feature.notes.state.NoteDetails
import java.util.Date

data object NotesPreviewData {

    val distortionsList = listOf(
        Distortion(
            id = 1,
            name = "Labeling",
            shortDescription = "",
            longDescription = "",
            examples = emptyList(),
            iconResId = R.drawable.ic_sample
        ),
        Distortion(
            id = 2,
            name = "Disqualifying the Positive",
            shortDescription = "",
            longDescription = "",
            examples = emptyList(),
            iconResId = R.drawable.ic_sample
        ),
    )

    val selectedEmotionsList = listOf(
        SelectedEmotion(
            emotion = Emotion(id = 1, name = "Joy", color = 12321),
            noteId = 0,
            intensityBefore = 15,
            intensityAfter = 100
        ),
        SelectedEmotion(
            emotion = Emotion(id = 2, name = "Angry", color = 12321),
            noteId = 0,
            intensityBefore = 15,
            intensityAfter = 100
        )
    )

    val thought = Thought(
        id = 0,
        text = "first thought first thought",
        alternativeThought = "alternative for first thought"
    )

    val note = Note(
        id = 1,
        date = Date(System.currentTimeMillis()),
        situation = "During the monthly team meeting, your manager enthusiastically praised one of your colleague's suggestions, describing it as 'brilliant' and 'game-changing.' However, they didn't acknowledge your earlier contributions, even though you spent considerable time preparing for the discussion. This made you feel unnoticed and undervalued.",
        bodyReaction = "Tightness in the chest, increased heart rate, and a sinking feeling in the stomach.",
        behavioralReaction = "Avoid speaking up for the rest of the meeting and begin doubting your abilities.",
        thoughts = listOf(thought, thought),
        emotions = selectedEmotionsList,
        distortions = distortionsList,
        distortionsIds = emptyList()
    )

    val shortNotes = listOf(
        ShortNote(
            id = 0,
            date = Date(System.currentTimeMillis()),
            situation = "During a team meeting, your manager praised a colleague's idea but did not comment on your contributions.",
            emotions = selectedEmotionsList
        ),
        ShortNote(
            id = 0,
            date = Date(System.currentTimeMillis()),
            situation = "During a team meeting, your manager praised a colleague's idea but did not comment on your contributions.",
            emotions = selectedEmotionsList
        )
    )

    val fieldsModifier: NoteModifier = object : NoteModifier {
        override fun updateDate(millis: Long) {}
        override fun updateTextField(noteDetails: NoteDetails) {}
        override fun toggleEmotionsDialog(isOpen: Boolean) {}
        override fun updateEmotionIntensityBefore(index: Int, intensity: Int) {}
        override fun removeEmotion(index: Int) {}
        override fun updateEmotionsList(ids: List<Long>) {}
        override fun toggleDistortionsDialog(isOpen: Boolean) {}
        override fun updateDistortionsList(ids: List<Long>) {}
        override fun removeDistortion(id: Long) {}
        override fun addThought() {}
        override fun removeThought(index: Int) {}
        override fun updateThoughtText(index: Int, text: String) {}
        override fun updateAlternativeThought(index: Int, alternative: String) {}
        override fun updateEmotionIntensityAfter(index: Int, intensity: Int) {}
    }
}
