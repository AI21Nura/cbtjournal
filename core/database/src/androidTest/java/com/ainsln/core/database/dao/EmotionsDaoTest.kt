package com.ainsln.core.database.dao

import com.ainsln.core.database.model.entity.SelectedEmotionCrossRef
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test

@HiltAndroidTest
class EmotionsDaoTest : BaseDaoTest() {

    @Test
    fun getEmotionsReturnsAllPreloadedEmotionsForEnglish() = runTest {
        val emotions = emotionsDao.getEmotions("en").first()
        assertThat(emotions).hasSize(30)
    }

    @Test
    fun getEmotionsReturnsAllPreloadedEmotionsForRussian() = runTest {
        val emotions = emotionsDao.getEmotions("ru").first()
        assertThat(emotions).hasSize(30)
    }

    @Test
    fun getEmotionsFiltersByIdsCorrectly() = runTest {
        val specificIds = listOf(1L, 5L, 10L)
        val filteredEmotions = emotionsDao.getEmotions("en", specificIds).first()

        assertThat(filteredEmotions).hasSize(3)
        assertThat(filteredEmotions.map { it.id }).containsExactlyElementsIn(specificIds)
    }

    @Test
    fun getSelectedEmotionsReturnsEmptyListInitially() = runTest {
        val selectedEmotions = emotionsDao.getSelectedEmotions("en").first()
        assertThat(selectedEmotions).isEmpty()
    }

    @Test
    fun insertSelectedEmotionFromPreloadedWorks() = runTest {

        val noteId = createTestNoteInDB()
        val selectedEmotion = SelectedEmotionCrossRef(
            noteId = noteId,
            emotionId = 1L,
            intensityBefore = 7,
            intensityAfter = 4
        )

        emotionsDao.insertEmotions(listOf(selectedEmotion))

        val result = emotionsDao.getSelectedEmotions("en", noteId).first()
        assertThat(result).hasSize(1)
        assertThat(result[0].selection).isEqualTo(selectedEmotion)
        assertThat(result[0].emotion.id).isEqualTo(1L)
    }

    @Test
    fun insertMultipleSelectedEmotionsWorks() = runTest {
        val noteId = createTestNoteInDB()
        val selectedEmotions = listOf(
            SelectedEmotionCrossRef(noteId, 1L, 8, 5),
            SelectedEmotionCrossRef(noteId, 2L, 6, 3),
            SelectedEmotionCrossRef(noteId, 3L, 7, 4)
        )

        emotionsDao.insertEmotions(selectedEmotions)

        val result = emotionsDao.getSelectedEmotions("en", noteId).first()
        assertThat(result).hasSize(3)
        assertThat(result.map { it.selection }).containsExactlyElementsIn(selectedEmotions)
    }

    @Test
    fun deleteSelectedEmotionsWorks() = runTest {
        val noteId = createTestNoteInDB()
        val selectedEmotions = listOf(
            SelectedEmotionCrossRef(noteId, 1L, 8, 5),
            SelectedEmotionCrossRef(noteId, 2L, 6, 3)
        )

        emotionsDao.insertEmotions(selectedEmotions)
        emotionsDao.deleteEmotions(listOf(selectedEmotions[0]))

        val result = emotionsDao.getSelectedEmotions("en", noteId).first()
        assertThat(result).hasSize(1)
        assertThat(result[0].selection).isEqualTo(selectedEmotions[1])
    }

    @Test
    fun getSelectedEmotionsWithoutNoteIdWorks() = runTest {
        val noteId1 = createTestNoteInDB()
        val noteId2 = createTestNoteInDB()
        val emotions1 = listOf(SelectedEmotionCrossRef(noteId1, 1L, 8, 5))
        val emotions2 = listOf(SelectedEmotionCrossRef(noteId2, 2L, 6, 3))

        emotionsDao.insertEmotions(emotions1)
        emotionsDao.insertEmotions(emotions2)

        val result = emotionsDao.getSelectedEmotions("en", null).first()
        assertThat(result).hasSize(2)
        assertThat(result.map { it.selection })
            .containsExactlyElementsIn(emotions1 + emotions2)
    }

    @Test
    fun selectedEmotionsReturnCorrectTranslationsWhenLanguageChanges() = runTest {
        val noteId = createTestNoteInDB()
        val selectedEmotion = SelectedEmotionCrossRef(noteId, 1L, 8, 5)
        emotionsDao.insertEmotions(listOf(selectedEmotion))

        val englishResult = emotionsDao.getSelectedEmotions("en", noteId).first()
        val englishName = englishResult[0].emotion.name

        val russianResult = emotionsDao.getSelectedEmotions("ru", noteId).first()
        val russianName = russianResult[0].emotion.name

        assertThat(englishName).isNotEqualTo(russianName)
        assertThat(englishResult[0].emotion.id).isEqualTo(russianResult[0].emotion.id)
        assertThat(englishResult[0].emotion.color).isEqualTo(russianResult[0].emotion.color)
    }

    @Test
    fun getEmotionsWithNonExistentLanguageReturnsEmptyList() = runTest {
        val result = emotionsDao.getEmotions("xx").first()
        assertThat(result).isEmpty()
    }

    @Test
    fun insertEmotionsReplacesExistingRecords() = runTest {
        val noteId = createTestNoteInDB()
        val initialEmotion = SelectedEmotionCrossRef(noteId, 1L, 8, 5)
        emotionsDao.insertEmotions(listOf(initialEmotion))

        val updatedEmotion = initialEmotion.copy(intensityBefore = 3, intensityAfter = 1)
        emotionsDao.insertEmotions(listOf(updatedEmotion))

        val result = emotionsDao.getSelectedEmotions("en", noteId).first()
        assertThat(result).hasSize(1)
        assertThat(result[0].selection).isEqualTo(updatedEmotion)
    }
}
