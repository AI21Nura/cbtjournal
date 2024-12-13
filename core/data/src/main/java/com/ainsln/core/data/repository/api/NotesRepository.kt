package com.ainsln.core.data.repository.api

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Note
import com.ainsln.core.model.ShortNote
import com.ainsln.core.model.Thought
import kotlinx.coroutines.flow.Flow

public interface NotesRepository {

    public fun getAllNotes(): Flow<Result<List<ShortNote>>>

    public fun getSearchNotes(query: String): Flow<Result<List<ShortNote>>>

    public fun getNoteById(id: Long): Flow<Result<Note>>

    public fun saveNoteWithThoughts(note: Note): Flow<Result<Long>>

    public fun deleteNote(note: Note): Flow<Result<Unit>>

    public suspend fun deleteThoughts(thoughts: List<Thought>, noteId: Long)

}
