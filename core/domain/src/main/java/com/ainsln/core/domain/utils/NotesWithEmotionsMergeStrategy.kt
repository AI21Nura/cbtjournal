package com.ainsln.core.domain.utils

import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.map
import com.ainsln.core.model.SelectedEmotion
import com.ainsln.core.model.ShortNote

typealias MergeStrategyForNotes = MergeStrategy<Result<List<ShortNote>>, Result<List<SelectedEmotion>>>

class NotesWithEmotionsMergeStrategy : MergeStrategyForNotes {

    override fun merge(
        notes: Result<List<ShortNote>>,
        emotions: Result<List<SelectedEmotion>>
    ): Result<List<ShortNote>> {
        return when {
            notes is Result.Success && emotions is Result.Success -> mergeSuccess(notes, emotions)
            notes is Result.Error -> Result.Error(notes.e)
            emotions is Result.Error -> Result.Error(emotions.e)
            else -> Result.Loading
        }
    }

    private fun mergeSuccess(
        notesResult: Result.Success<List<ShortNote>>,
        emotionsResult: Result.Success<List<SelectedEmotion>>
    ): Result<List<ShortNote>> {
        val emotions = emotionsResult.data
        return notesResult.map { notes ->
            notes.map { note ->
                note.copy(emotions = emotions.filter { it.noteId == note.id })
            }
        }
    }


}
