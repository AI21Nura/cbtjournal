package com.ainsln.core.domain

import com.ainsln.core.data.repository.RoomEmotionsRepository
import com.ainsln.core.data.repository.api.NotesRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.domain.utils.MergeStrategyForNotes
import com.ainsln.core.domain.utils.NotesWithEmotionsMergeStrategy
import com.ainsln.core.model.ShortNote
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchNotesUseCase @Inject constructor(
    private val notesRepository: NotesRepository,
    private val emotionsRepository: RoomEmotionsRepository,
) {
    operator fun invoke(
        query: String,
        mergeStrategy: MergeStrategyForNotes = NotesWithEmotionsMergeStrategy()
    ): Flow<Result<List<ShortNote>>> {
        return combine(
            notesRepository.getSearchNotes(prepareSearchQuery(query)),
            emotionsRepository.getAllSelectedEmotions(),
            mergeStrategy::merge
        )
    }

    private fun prepareSearchQuery(query: String): String {
        val queryWithEscapedQuotes = query.replace(Regex.fromLiteral("\""), "\"\"")
        return "\"*$queryWithEscapedQuotes*\""
    }
}
