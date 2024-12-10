package com.ainsln.feature.notes.editor.tabs

import com.ainsln.feature.notes.R


enum class EditorScreenTab(val index: Int, val titleResId: Int) {
    Situation(index = 0, titleResId = R.string.situation_tab),
    Interpretation(index = 1, titleResId = R.string.interpretation_tab),
    Reframing(index = 2, titleResId = R.string.reframing_tab)
}
