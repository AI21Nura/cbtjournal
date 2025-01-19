package com.ainsln.core.database.dao

import com.ainsln.core.database.CBTDatabase
import com.ainsln.core.database.model.entity.NoteEntity
import com.ainsln.core.database.model.entity.ThoughtEntity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

open class BaseDaoTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("without_notes")
    internal lateinit var database: CBTDatabase

    protected lateinit var notesDao: NotesDao
    protected lateinit var emotionsDao: EmotionsDao
    protected lateinit var recentSearchesDao: RecentSearchesDao

    @Before
    fun setup() {
        hiltRule.inject()
        notesDao = database.notesDao()
        emotionsDao = database.emotionsDao()
        recentSearchesDao = database.recentSearchesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    protected fun createTestNote(situation: String = "Test situation"): NoteEntity {
        return NoteEntity(
                id = 0,
                date = Date(),
                situation = situation,
                bodyReaction = "Test body reaction",
                behavioralReaction = "Test behavioral reaction",
                distortionsIds = listOf(1L, 2L)
            )
    }

    protected suspend fun createTestNoteInDB(): Long {
        return notesDao.insertNote(createTestNote())
    }

    protected fun createTestThought(
        id: Long = 0,
        noteId: Long,
        text: String = "Test thought",
        alternativeThought: String = "Alternative thought"
    ) = ThoughtEntity(
        id = id,
        noteId = noteId,
        text = text,
        alternativeThought = alternativeThought
    )
}
