package com.ainsln.data

import android.content.Context
import com.ainsln.core.model.Distortion
import com.ainsln.feature.distortions.R

data object DistortionsPreviewData {

    fun getDistortionsList(context: Context) = listOf(
        Distortion(
            id = 1,
            name = context.getString(R.string.distortion_1_name),
            shortDescription = context.getString(R.string.distortion_1_short_desc),
            longDescription = context.getString(R.string.distortion_1_long_desc),
            examples = emptyList(),
            iconResId = R.drawable.ic_distortion_1
        ),
        Distortion(
            id = 2,
            name = context.getString(R.string.distortion_2_name),
            shortDescription = context.getString(R.string.distortion_2_short_desc),
            longDescription = context.getString(R.string.distortion_2_long_desc),
            examples = emptyList(),
            iconResId = R.drawable.ic_distortion_2
        ),
    )

    fun getDistortion(context: Context) = Distortion(
        id = 1,
        name = context.getString(R.string.distortion_1_name),
        shortDescription = context.getString(R.string.distortion_1_short_desc),
        longDescription = context.getString(R.string.distortion_1_long_desc),
        examples = context.resources.getStringArray(R.array.distortion_1_examples).toList(),
        iconResId = R.drawable.ic_distortion_1
    )

}
