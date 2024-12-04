package com.ainsln.core.domain

import com.ainsln.core.data.repository.RoomEmotionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.model.SelectedEmotion
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf

class GetSelectedEmotionsUseCase @Inject constructor(
    private val emotionsRepository: RoomEmotionsRepository
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(ids: List<Long>): Flow<Result<List<SelectedEmotion>>> {
        return emotionsRepository.getEmotionsByIds(ids).flatMapConcat { emotionsResult ->
            flowOf(when (emotionsResult) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(emotionsResult.e)
                is Result.Success -> {
                    val selectedEmotions = mutableListOf<SelectedEmotion>().apply {
                        emotionsResult.data.forEach { emotion ->
                            add(
                                SelectedEmotion(
                                    emotion = emotion,
                                    noteId = 0,
                                    intensityBefore = 15,
                                    intensityAfter = 15
                                )
                            )
                        }
                    }
                    Result.Success(selectedEmotions)
                }
            })
        }
    }
}
