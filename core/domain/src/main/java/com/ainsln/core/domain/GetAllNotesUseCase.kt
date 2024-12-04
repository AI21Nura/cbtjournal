package com.ainsln.core.domain

import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.data.repository.RoomEmotionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.domain.utils.MergeStrategyForNotes
import com.ainsln.core.domain.utils.NotesWithEmotionsMergeStrategy
import com.ainsln.core.model.ShortNote
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetAllNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val emotionsRepository: RoomEmotionsRepository,
    ) {

    operator fun invoke(
        langCode: String,
        mergeStrategy: MergeStrategyForNotes
            = NotesWithEmotionsMergeStrategy()
    ): Flow<Result<List<ShortNote>>> {
        return combine(
            notesRepository.getAllNotes(),
            emotionsRepository.getAllSelectedEmotions(langCode),
            mergeStrategy::merge
        )
    }
}
