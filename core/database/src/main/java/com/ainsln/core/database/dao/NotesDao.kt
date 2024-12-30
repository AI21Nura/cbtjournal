package com.ainsln.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ainsln.core.database.model.NoteWithThoughts
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.ThoughtEntity
import kotlinx.coroutines.flow.Flow

@Dao
public interface NotesDao {

    @Transaction
    @Query("SELECT * FROM Note")
    public fun getAll(): Flow<List<NoteEntity>>

    @Transaction
    @Query("""
        SELECT * FROM Note
        JOIN NoteFts ON Note.id = NoteFts.id 
        WHERE NoteFts MATCH :query 
        """)
    public fun getSearch(query: String): Flow<List<NoteEntity>>

    @Transaction
    @Query("SELECT * FROM Note WHERE id=:id")
    public fun getById(id: Long): Flow<NoteWithThoughts>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insertNote(note: NoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public suspend fun insertThoughts(thoughts: List<ThoughtEntity>)

    @Delete
    public suspend fun deleteNote(note: NoteEntity)

    @Query("DELETE FROM Note WHERE id IN (:ids)")
    public suspend fun deleteNotesById(ids: List<Long>)

    @Delete
    public suspend fun deleteThoughts(thoughts: List<ThoughtEntity>)
}
