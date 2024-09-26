package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ainsln.core.database.model.NoteWithThoughts
import com.ainsln.core.database.model.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
public interface NotesDao {

    @Transaction
    @Query("SELECT * FROM Note")
    public fun getAll(): Flow<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM Note WHERE id=:id")
    public fun getById(id: Long): Flow<NoteWithThoughts>

}
