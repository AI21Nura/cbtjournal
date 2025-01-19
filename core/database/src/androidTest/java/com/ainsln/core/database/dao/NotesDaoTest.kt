package com.ainsln.core.database.dao

import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@HiltAndroidTest
class NotesDaoTest : BaseDaoTest() {

    @Test
    fun getAllReturnsEmptyListInitially() = runTest {
        val notes = notesDao.getAll().first()
        assertThat(notes).isEmpty()
    }

    @Test
    fun insertNoteWorks() = runTest {
        val noteId = createTestNoteInDB()

        val result = notesDao.getAll().first()

        assertThat(result).hasSize(1)
        assertThat(result[0].id).isEqualTo(noteId)
    }

    @Test
    fun insertNoteWithThoughtsWorks() = runTest {
        val noteId = createTestNoteInDB()

        val thoughts = listOf(
            createTestThought(noteId = noteId, text = "First thought"),
            createTestThought(noteId = noteId, text = "Second thought")
        )
        notesDao.insertThoughts(thoughts)

        val result = notesDao.getById(noteId).first()
        assertThat(result.thoughts).hasSize(2)
        assertThat(result.thoughts.map { it.text })
            .containsExactlyElementsIn(thoughts.map { it.text })
    }

    @Test
    fun replaceExistingNoteWorks() = runTest {
        val note = createTestNote()
        val noteId = notesDao.insertNote(note)

        val updatedNote = note.copy(
            id = noteId,
            situation = "Updated situation"
        )
        notesDao.insertNote(updatedNote)

        val result = notesDao.getAll().first()
        assertThat(result).hasSize(1)
        assertThat(result[0].situation).isEqualTo("Updated situation")
    }

    @Test
    fun searchNotesWorks() = runTest {
        val note1 = createTestNote("Unique situation alpha")
        val note2 = createTestNote(situation = "Different situation beta")

        val noteId1 = notesDao.insertNote(note1)
        notesDao.insertNote(note2)

        val searchResults = notesDao.getSearch("alpha").first()
        assertThat(searchResults).hasSize(1)
        assertThat(searchResults[0].id).isEqualTo(noteId1)
    }

    @Test
    fun deleteNoteWorks() = runTest {
        val note = createTestNote()
        val noteId = notesDao.insertNote(note)

        val thoughts = listOf(
            createTestThought(noteId = noteId),
            createTestThought(noteId = noteId)
        )
        notesDao.insertThoughts(thoughts)

        notesDao.deleteNote(note.copy(id = noteId))

        val resultNote = notesDao.getById(noteId).first()
        assertThat(resultNote).isNull()
    }

    @Test
    fun deleteNoteCascadesToSelectedEmotions() = runTest {
        val note = createTestNote()
        val noteId = notesDao.insertNote(note)

        val selectedEmotions = listOf(
            SelectedEmotionCrossRef(noteId, 1L, 8, 5),
            SelectedEmotionCrossRef(noteId, 2L, 6, 3)
        )
        emotionsDao.insertEmotions(selectedEmotions)

        notesDao.deleteNote(note.copy(id = noteId))

        val remainingEmotions = emotionsDao.getSelectedEmotions("en", noteId).first()
        assertThat(remainingEmotions).isEmpty()
    }

    @Test
    fun deleteNotesByIdWorks() = runTest {
        val note1 = createTestNote()
        val note2 = createTestNote()
        val note3 = createTestNote()

        val id1 = notesDao.insertNote(note1)
        val id2 = notesDao.insertNote(note2)
        val id3 = notesDao.insertNote(note3)

        notesDao.deleteNotesById(listOf(id1, id2))

        val remainingNotes = notesDao.getAll().first()
        assertThat(remainingNotes).hasSize(1)
        assertThat(remainingNotes[0].id).isEqualTo(id3)
    }

    @Test
    fun deleteSpecificThoughtsWorks() = runTest {
        val noteId = createTestNoteInDB()

        val thoughts = listOf(
            createTestThought(noteId = noteId, text = "Keep this"),
            createTestThought(noteId = noteId, text = "Delete this")
        )
        notesDao.insertThoughts(thoughts)

        val actualThoughts = notesDao.getById(noteId).first().thoughts
        val thoughtToDelete = actualThoughts.find { it.text == "Delete this" }
        assertThat(thoughtToDelete).isNotNull()
        notesDao.deleteThoughts(listOf(thoughtToDelete!!))

        val result = notesDao.getById(noteId).first()
        assertThat(result.thoughts).hasSize(1)
        assertThat(result.thoughts[0].text).isEqualTo("Keep this")
    }

    @Test
    fun handleLongTextFieldsWorks() = runTest {
        val longText = "a".repeat(1000)
        val note = createTestNote(situation = longText)
        val noteId = notesDao.insertNote(note)

        val thought = createTestThought(
            noteId = noteId,
            text = longText,
            alternativeThought = longText
        )
        notesDao.insertThoughts(listOf(thought))

        val result = notesDao.getById(noteId).first()
        assertThat(result.note.situation).hasLength(1000)
    }

    @Test
    fun searchWithSpecialCharactersWorks() = runTest {
        val notes = listOf(
            createTestNote(situation = "Test with @#$%^&* special chars"),
            createTestNote(situation = "Test without special chars"),
            createTestNote(situation = "Another test @ with # symbols")
        )

        notes.forEach { notesDao.insertNote(it) }

        val resultsForTest = notesDao.getSearch(prepareSearchQuery("test")).first()
        assertThat(resultsForTest).hasSize(3)

        val resultsForTes = notesDao.getSearch(prepareSearchQuery("tes")).first()
        assertThat(resultsForTes).hasSize(3)

        val resultsForWith = notesDao.getSearch(prepareSearchQuery("test with")).first()
        assertThat(resultsForWith).hasSize(3)
    }

    @Test
    fun searchWithEmptyQueryWorks() = runTest {
        createTestNoteInDB()
        val results = notesDao.getSearch(prepareSearchQuery("")).first()
        assertThat(results).isEmpty()
    }

    @Test
    fun searchIsCaseInsensitive() = runTest {
        val note = createTestNote(situation = "Test CASE sensitivity")
        notesDao.insertNote(note)

        val resultsLower = notesDao.getSearch(prepareSearchQuery("test")).first()
        val resultsUpper = notesDao.getSearch(prepareSearchQuery("TEST")).first()
        val resultsForCase = notesDao.getSearch(prepareSearchQuery("case")).first()

        assertThat(resultsLower).hasSize(1)
        assertThat(resultsUpper).hasSize(1)
        assertThat(resultsForCase).hasSize(1)
    }

    @Test
    fun searchWithHyphensAndApostrophes() = runTest {
        val notes = listOf(
            createTestNote(situation = "well-being test"),
            createTestNote(situation = "don't worry test"),
            createTestNote(situation = "well being test")
        )
        notes.forEach { notesDao.insertNote(it) }

        val resultsForWellBeing = notesDao.getSearch(prepareSearchQuery("well being")).first()
        assertThat(resultsForWellBeing).hasSize(2)

        val resultsForDont = notesDao.getSearch(prepareSearchQuery("dont")).first()
        val resultsForDo = notesDao.getSearch(prepareSearchQuery("don't")).first()
        assertThat(resultsForDont).hasSize(0)
        assertThat(resultsForDo).hasSize(1)
    }

    @Test
    fun complexScenarioWorks() = runTest {
        val note1 = createTestNote("Situation 1")
        val note2 = createTestNote("Situation 2")
        val id1 = notesDao.insertNote(note1)
        val id2 = notesDao.insertNote(note2)

        val thoughts1 = listOf(
            createTestThought(noteId = id1, text = "Thought 1.1"),
            createTestThought(noteId = id1, text = "Thought 1.2")
        )
        val thoughts2 = listOf(
            createTestThought(noteId = id2, text = "Thought 2.1")
        )
        notesDao.insertThoughts(thoughts1)
        notesDao.insertThoughts(thoughts2)

        val emotions1 = listOf(
            SelectedEmotionCrossRef(id1, 1L, 8, 5),
            SelectedEmotionCrossRef(id1, 2L, 7, 4)
        )
        val emotions2 = listOf(
            SelectedEmotionCrossRef(id2, 3L, 6, 3)
        )
        emotionsDao.insertEmotions(emotions1)
        emotionsDao.insertEmotions(emotions2)

        val noteWithThoughts1 = notesDao.getById(id1).first()
        assertThat(noteWithThoughts1.thoughts).hasSize(2)

        val selectedEmotions1 = emotionsDao.getSelectedEmotions("en", id1).first()
        assertThat(selectedEmotions1).hasSize(2)

        notesDao.deleteNote(note1.copy(id = id1))

        val remainingNotes = notesDao.getAll().first()
        assertThat(remainingNotes).hasSize(1)

        val remainingEmotions = emotionsDao.getSelectedEmotions("en", null).first()
        assertThat(remainingEmotions).hasSize(1)
        assertThat(remainingEmotions[0].selection.noteId).isEqualTo(id2)
    }

    private fun prepareSearchQuery(query: String): String {
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "\"*$queryWithEscapedQuotes*\""
    }
}


