package com.ainsln.feature.notes.utils

import com.ainsln.core.data.util.ResourceManager
import com.ainsln.core.model.Note
import com.ainsln.feature.notes.R
import javax.inject.Inject

class NoteFormatter @Inject constructor(
    private val resourceManager: ResourceManager
) {
    fun buildTextForSending(note: Note): String {
        var text = ""
        text += getString(R.string.date_label) + ":\n" + formatDate(note.date) + "\n\n"
        text += getString(R.string.situation_label) + ":\n" + note.situation + "\n\n"

        if (note.bodyReaction.isNotBlank())
            text += getString(R.string.body_reaction_label) + ":\n" + note.bodyReaction + "\n\n"
        if (note.behavioralReaction.isNotBlank())
            text += getString(R.string.behavioral_reaction_label) + ":\n" + note.bodyReaction + "\n\n"

        text += getString(R.string.emotions_before_label) + ":\n"
        note.emotions.forEach { emotion ->
            text += emotion.emotion.name + " - " + emotion.intensityBefore.toString() + "%\n"
        }

        if (note.thoughts.isNotEmpty()){
            text += "\n" + getString(R.string.thoughts_and_alternatives_label) + ":\n"
            note.thoughts.forEachIndexed { index, thought ->
                text += getString(R.string.thought_number, index + 1) + ": " + thought.text + "\n"
                text += getString(R.string.alternative) + ": " + thought.alternativeThought + "\n\n"
            }
        }

        if (note.distortions.isNotEmpty()){
            text += getString(R.string.distortions_label) + ":\n"
            note.distortions.forEach { distortion ->
                text += distortion.name + "\n"
            }
        }

        text += "\n" + getString(R.string.emotions_after_label) + ":\n"
        note.emotions.forEach { emotion ->
            text += emotion.emotion.name + " - " + emotion.intensityAfter.toString() + "%\n"
        }

        return text
    }

    private fun getString(stringResId: Int, vararg formatArgs: Any) =
        resourceManager.getString(stringResId, *formatArgs)
}
