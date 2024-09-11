package com.ainsln.core.model

import androidx.annotation.DrawableRes

public data class Distortion(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val longDescription: String,
    val examples: List<String>,
    @DrawableRes val iconResId: Int
)
