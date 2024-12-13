package com.ainsln.core.data.repository

import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.processFlowList
import com.ainsln.core.data.util.toNote
import com.ainsln.core.data.util.toNoteEntity
import com.ainsln.core.data.util.toShortNote
import com.ainsln.core.data.util.toThoughtEntity
import com.ainsln.core.database.dao.NotesDao
import com.ainsln.core.model.Note
import com.ainsln.core.model.ShortNote
import com.ainsln.core.model.Thought
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class RoomNotesRepository @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override fun getAllNotes(): Flow<Result<List<ShortNote>>> {
        return processFlowList(notesDao.getAll()) { it.toShortNote() }
    }

    override fun getSearchNotes(query: String): Flow<Result<List<ShortNote>>> {
        return processFlowList(notesDao.getSearch(query)) { it.toShortNote() }
    }

    override fun getNoteById(id: Long): Flow<Result<Note>> = flow {
        try {
            notesDao.getById(id).collect { data ->
                emit(Result.Success(data.toNote()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.onStart { emit(Result.Loading) }

    override fun saveNoteWithThoughts(note: Note) = flow<Result<Long>> {
        val noteId = notesDao.insertNote(note.toNoteEntity())
        notesDao.insertThoughts(note.thoughts.map {
            it.toThoughtEntity(noteId)
        })
        emit(Result.Success(noteId))
    }
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(e)) }

    override fun deleteNote(note: Note) = flow<Result<Unit>> {
        notesDao.deleteNote(note.toNoteEntity())
        emit(Result.Success(Unit))
    }
        .onStart { emit(Result.Loading) }
        .catch { e -> emit(Result.Error(e)) }

    override suspend fun deleteThoughts(thoughts: List<Thought>, noteId: Long) {
        notesDao.deleteThoughts(thoughts.map { it.toThoughtEntity(noteId) })
    }
}
