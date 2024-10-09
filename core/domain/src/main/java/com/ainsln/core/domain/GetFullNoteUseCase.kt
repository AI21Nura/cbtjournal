package com.ainsln.core.domain

import com.ainsln.core.data.repository.DistortionsRepository
import com.ainsln.core.data.repository.NotesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.map
import com.ainsln.core.model.Note
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class GetFullNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val distortionsRepository: DistortionsRepository
) {
    operator fun invoke(id: Long): Flow<Result<Note>> {
        return notesRepository.getNoteById(id).flatMapConcat { noteResult ->
            if (noteResult is Result.Success) {
                val distortionsFlow =
                    distortionsRepository.getDistortionsByIds(noteResult.data.distortionsIds)

                distortionsFlow.map { distortionsResult ->
                    distortionsResult.map { distortions ->
                        noteResult.data.copy(distortions = distortions)
                    }
                }

            } else {
                flowOf(noteResult)
            }
        }
    }

}
