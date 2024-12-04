package com.ainsln.feature.notes.components.circle

sealed interface CircleClickResult {
    data object OutsideCircleClick : CircleClickResult

    data class SectorClicked(
        val level: Int,
        val sector: Int,
    ) : CircleClickResult
}
