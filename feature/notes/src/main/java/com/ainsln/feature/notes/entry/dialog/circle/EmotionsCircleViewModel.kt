package com.ainsln.feature.notes.entry.dialog.circle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.api.EmotionsRepository
import com.ainsln.core.data.result.Result
import com.ainsln.core.model.Emotion
import com.ainsln.feature.notes.components.circle.Circle
import com.ainsln.feature.notes.components.circle.CircleLevel
import com.ainsln.feature.notes.components.circle.Sector
import com.ainsln.feature.notes.state.EmotionsDialogUiState
import com.ainsln.feature.notes.state.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class EmotionsCircleViewModel @Inject constructor(
    emotionsRepository: EmotionsRepository
) : ViewModel() {
    private val selectedIds = MutableStateFlow(mutableListOf<Long>())
    private val emotionsFlow = emotionsRepository.getAllEmotions()
    private val circleSizePx = MutableStateFlow(600f)

    val circleUiState: StateFlow<EmotionsDialogUiState> = combine(
        emotionsFlow,
        circleSizePx,
        selectedIds
    ){ emotionsResult, circleSize, selectedIds ->
        if (emotionsResult is Result.Success){
            EmotionsDialogUiState.Success(initCircle(emotionsResult.data, circleSize, selectedIds))
        } else {
            emotionsResult.toUiState()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EmotionsDialogUiState.Loading
    )

    private fun getSectorsFromEmotions(
        emotions: List<Emotion>,
        numberOfBaseEmotions: Int,
        selectedIds: List<Long>
    ) : List<List<Sector>> {
        val sectors = emotions.map {
            Sector(
                id = it.id,
                label = it.name,
                color = Color(it.color),
                isSelected = selectedIds.contains(it.id)
            )
        }

        return listOf(
            sectors.subList(0, numberOfBaseEmotions),
            sectors.subList(numberOfBaseEmotions, sectors.size)
        )
    }

    private fun initCircle(
        emotions: List<Emotion>,
        circleSizePx: Float,
        selectedIds: List<Long>,
        numberOfBaseEmotions: Int = 6
    ) : Circle{
        if (emotions.size < numberOfBaseEmotions) return Circle.DEFAULT

        val objects = getSectorsFromEmotions(emotions, numberOfBaseEmotions, selectedIds)
        val sectorLevelsNumber = objects.size

        val radius = circleSizePx / 2
        val radiusForLevels =
            listOf(0f) + List(sectorLevelsNumber) { ind -> (radius / sectorLevelsNumber) * (ind + 1) }

        return Circle(
                center = Offset(radius, radius),
                radius = radius,
                levels =
                MutableList(sectorLevelsNumber) { levelIndex ->
                    CircleLevel(
                        sectorsNumber = objects[levelIndex].size,
                        outerRadius = radiusForLevels[levelIndex + 1],
                        innerRadius = radiusForLevels[levelIndex],
                        sectorAngle = 360f / objects[levelIndex].size,
                        sectors = objects[levelIndex],
                        averageTextLength =
                        objects[levelIndex]
                            .map { it.label.length }
                            .average()
                            .toInt(),
                    )
                }
        )
    }

    fun setInitSelectedList(ids: List<Long>) {
        selectedIds.value = ids.toMutableList()
    }

    fun addSelection(id: Long){
        selectedIds.value.add(id)
    }

    fun removeSelection(id: Long){
        selectedIds.value.remove(id)
    }

    fun getSelectedList(): List<Long> = selectedIds.value

    fun updateCircleSize(size: Float){
        if (size > 0f){
            circleSizePx.value = size
        }
    }

}
