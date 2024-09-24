package com.ainsln.core.datastore.model

import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

public data class DistortionStore(
    val id: Long,
    @StringRes val name: Int,
    @StringRes val shortDescription: Int,
    @StringRes val longDescription: Int,
    @ArrayRes val examples: Int,
    @DrawableRes val iconResId: Int
)
