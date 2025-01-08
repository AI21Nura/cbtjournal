package com.ainsln.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val SelectedItem = Color(0xFFdde2f8)
val SelectedItemDark = Color(0xFF3D3D3D)
val FaqSection = Color(0xFF4D90A6)
val FaqSectionDark = Color(0xFF27348B)

val SelectedItemColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) SelectedItemDark else SelectedItem

val ClickableTextColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.LightGray else Color.Blue

val FaqSectionColor: Color
    @Composable
    get() = if(isSystemInDarkTheme()) FaqSectionDark else FaqSection
