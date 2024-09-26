package com.ainsln.core.data.repository

import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Note
import com.ainsln.core.model.ShortNote
import kotlinx.coroutines.flow.Flow

public interface NotesRepository {

    public fun getNotes(): Flow<Result<List<ShortNote>>>

    public fun getNoteById(id: Long): Flow<Result<Note>>

}
