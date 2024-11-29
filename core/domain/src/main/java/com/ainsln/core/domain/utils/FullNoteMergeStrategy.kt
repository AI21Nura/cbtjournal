package com.ainsln.core.domain.utils

import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.map
import com.ainsln.core.model.Distortion
import com.ainsln.core.model.Note
import com.ainsln.core.model.SelectedEmotion

typealias MergeStrategyForFullNote
        = MergeStrategyTriple<Result<Note>, Result<List<Distortion>>, Result<List<SelectedEmotion>>>

class FullNoteMergeStrategy : MergeStrategyForFullNote {

    override fun merge(
        note: Result<Note>,
        distortions: Result<List<Distortion>>,
        emotions: Result<List<SelectedEmotion>>
    ): Result<Note> {
        return when {
            note is Result.Success && distortions is Result.Success && emotions is Result.Success ->
                mergeSuccess(note, distortions, emotions)

            note is Result.Error -> Result.Error(note.e)
            distortions is Result.Error -> Result.Error(distortions.e)
            emotions is Result.Error -> Result.Error(emotions.e)
            else -> Result.Loading
        }
    }

    private fun mergeSuccess(
        noteResult: Result.Success<Note>,
        distortionsResult: Result.Success<List<Distortion>>,
        emotionsResult: Result.Success<List<SelectedEmotion>>
    ): Result<Note> {
        return noteResult.map { note ->
            note.copy(
                distortions = distortionsResult.data,
                emotions = emotionsResult.data
            )
        }
    }
}
