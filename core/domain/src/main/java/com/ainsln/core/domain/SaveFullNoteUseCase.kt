package com.ainsln.core.domain

import com.ainsln.core.data.repository.api.EmotionsRepository
import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Note
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

@OptIn(ExperimentalCoroutinesApi::class)
class SaveFullNoteUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val emotionsRepository: EmotionsRepository
) {
    operator fun invoke(newNote: Note, oldNote: Note? = null): Flow<Result<Long>> {
        return notesRepository.saveNoteWithThoughts(newNote).flatMapConcat { result ->
            flow {
                if (result is Result.Success) {
                    oldNote?.let {
                        val removedThoughts = oldNote.thoughts.filter { it !in newNote.thoughts }
                        val removedEmotions = oldNote.emotions.filter { it !in newNote.emotions }
                        notesRepository.deleteThoughts(removedThoughts, newNote.id)
                        emotionsRepository.deleteSelectedEmotions(removedEmotions, newNote.id)
                    }
                    emotionsRepository.saveSelectedEmotions(newNote.emotions, result.data)
                }
                emit(result)
            }.catch { e -> emit(Result.Error(e)) }
        }.onStart { emit(Result.Loading) }
    }
}
