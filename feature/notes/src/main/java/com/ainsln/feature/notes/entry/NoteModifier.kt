package com.ainsln.feature.notes.entry

import com.ainsln.feature.notes.state.NoteDetails

interface NoteModifier
    : SituationFieldsModifier, InterpretationFieldsModifier, ReframingFieldsModifier

interface SituationFieldsModifier {
    fun updateDate(millis: Long)

    fun updateTextField(noteDetails: NoteDetails)

    fun toggleEmotionsDialog(isOpen: Boolean)

    fun updateEmotionIntensityBefore(index: Int, intensity: Int)

    fun removeEmotion(index: Int)

    fun updateEmotionsList(ids: List<Long>)
}

interface InterpretationFieldsModifier {
    fun toggleDistortionsDialog(isOpen: Boolean)

    fun updateDistortionsList(ids: List<Long>)

    fun removeDistortion(id: Long)

    fun addThought()

    fun removeThought(index: Int)

    fun updateThoughtText(index: Int, text: String)
}

interface ReframingFieldsModifier {
    fun updateAlternativeThought(index: Int, alternative: String)
    fun updateEmotionIntensityAfter(index: Int, intensity: Int)
}
