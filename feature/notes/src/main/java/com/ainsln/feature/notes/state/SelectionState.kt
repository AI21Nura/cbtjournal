package com.ainsln.feature.notes.state

import androidx.compose.runtime.mutableStateListOf

data class SelectionState(
    val isSelectionMode: Boolean = false,
    val selectedItems: MutableList<Long> = mutableStateListOf()
)
