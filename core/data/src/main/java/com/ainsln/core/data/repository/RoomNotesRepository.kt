package com.ainsln.core.data.repository

import com.ainsln.core.data.result.Result
import com.ainsln.core.data.result.asFlowResult
import com.ainsln.core.data.result.map
import com.ainsln.core.data.util.toNote
import com.ainsln.core.data.util.toShortNote
import com.ainsln.core.database.dao.NotesDao
import com.ainsln.core.model.Note
import com.ainsln.core.model.ShortNote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RoomNotesRepository @Inject constructor(
    private val notesDao: NotesDao
) : NotesRepository {

    override fun getNotes(): Flow<Result<List<ShortNote>>> {
        return notesDao.getAll().asFlowResult().map { result ->
            result.map { list -> list.map { it.toShortNote() } }
        }
    }

    override fun getNoteById(id: Long): Flow<Result<Note>> {
        return notesDao.getById(id).asFlowResult().map { result ->
            result.map { it.toNote() }
        }
    }

}
