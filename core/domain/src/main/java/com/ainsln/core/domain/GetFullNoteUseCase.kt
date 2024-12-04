package com.ainsln.core.domain

import com.ainsln.core.data.repository.api.DistortionsRepository
import com.ainsln.core.data.repository.api.EmotionsRepository
import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.domain.utils.FullNoteMergeStrategy
import com.ainsln.core.domain.utils.MergeStrategyForFullNote
import com.ainsln.core.model.Note
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class GetFullNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val emotionsRepository: EmotionsRepository,
    private val distortionsRepository: DistortionsRepository
) {
    operator fun invoke(
        id: Long,
        mergeStrategy: MergeStrategyForFullNote = FullNoteMergeStrategy()
    ): Flow<Result<Note>> {
        return notesRepository.getNoteById(id).flatMapConcat { noteResult ->
            if (noteResult is Result.Success) {
                combine(
                    emotionsRepository.getSelectedByNoteId(id),
                    distortionsRepository.getDistortionsByIds(noteResult.data.distortionsIds),
                ){ emotionsResult, distortionsResult ->
                    mergeStrategy.merge(
                        noteResult,
                        distortionsResult,
                        emotionsResult
                    )
                }
            } else {
                flowOf(noteResult)
            }
        }
    }

}
