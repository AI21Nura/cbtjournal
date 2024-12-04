package com.ainsln.feature.notes.components.circle

import androidx.compose.ui.graphics.Color

data class Sector(
    val id: Long,
    val label: String,
    val color: Color,
    val isSelected: Boolean,
)
