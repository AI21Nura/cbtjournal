package com.ainsln.core.datastore.distortion

import com.ainsln.core.data.R
import com.ainsln.core.resources.R.drawable
import com.ainsln.core.datastore.model.DistortionStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public object DistortionsResourcesDataSource : DistortionsDataSource<DistortionStore> {

    private val cognitiveDistortions: List<DistortionStore> = listOf(
        DistortionStore(
            id = 1,
            name = R.string.distortion_1_name,
            shortDescription = R.string.distortion_1_short_desc,
            longDescription = R.string.distortion_1_long_desc,
            examples = R.array.distortion_1_examples,
            iconResId = drawable.ic_distortion_1
        ),

        DistortionStore(
            id = 2,
            name = R.string.distortion_2_name,
            shortDescription = R.string.distortion_2_short_desc,
            longDescription = R.string.distortion_2_long_desc,
            examples = R.array.distortion_2_examples,
            iconResId = drawable.ic_distortion_2
        ),

        DistortionStore(
            id = 3,
            name = R.string.distortion_3_name,
            shortDescription = R.string.distortion_3_short_desc,
            longDescription = R.string.distortion_3_long_desc,
            examples = R.array.distortion_3_examples,
            iconResId = drawable.ic_distortion_3
        ),

        DistortionStore(
            id = 4,
            name = R.string.distortion_4_name,
            shortDescription = R.string.distortion_4_short_desc,
            longDescription = R.string.distortion_4_long_desc,
            examples = R.array.distortion_4_examples,
            iconResId = drawable.ic_distortion_4
        ),

        DistortionStore(
            id = 5,
            name = R.string.distortion_5_name,
            shortDescription = R.string.distortion_5_short_desc,
            longDescription = R.string.distortion_5_long_desc,
            examples = R.array.distortion_5_examples,
            iconResId = drawable.ic_distortion_5
        ),

        DistortionStore(
            id = 6,
            name = R.string.distortion_6_name,
            shortDescription = R.string.distortion_6_short_desc,
            longDescription = R.string.distortion_6_long_desc,
            examples = R.array.distortion_6_examples,
            iconResId = drawable.ic_distortion_6
        ),

        DistortionStore(
            id = 7,
            name = R.string.distortion_7_name,
            shortDescription = R.string.distortion_7_short_desc,
            longDescription = R.string.distortion_7_long_desc,
            examples = R.array.distortion_7_examples,
            iconResId = drawable.ic_distortion_7
        ),

        DistortionStore(
            id = 8,
            name = R.string.distortion_8_name,
            shortDescription = R.string.distortion_8_short_desc,
            longDescription = R.string.distortion_8_long_desc,
            examples = R.array.distortion_8_examples,
            iconResId = drawable.ic_distortion_8
        ),

        DistortionStore(
            id = 9,
            name = R.string.distortion_9_name,
            shortDescription = R.string.distortion_9_short_desc,
            longDescription = R.string.distortion_9_long_desc,
            examples = R.array.distortion_9_examples,
            iconResId = drawable.ic_distortion_9
        ),

        DistortionStore(
            id = 10,
            name = R.string.distortion_10_name,
            shortDescription = R.string.distortion_10_short_desc,
            longDescription = R.string.distortion_10_long_desc,
            examples = R.array.distortion_10_examples,
            iconResId = drawable.ic_distortion_10
        ),

        DistortionStore(
            id = 11,
            name = R.string.distortion_11_name,
            shortDescription = R.string.distortion_11_short_desc,
            longDescription = R.string.distortion_11_long_desc,
            examples = R.array.distortion_11_examples,
            iconResId = drawable.ic_distortion_11
        ),

        DistortionStore(
            id = 12,
            name = R.string.distortion_12_name,
            shortDescription = R.string.distortion_12_short_desc,
            longDescription = R.string.distortion_12_long_desc,
            examples = R.array.distortion_12_examples,
            iconResId = drawable.ic_distortion_12
        ),

        DistortionStore(
            id = 13,
            name = R.string.distortion_13_name,
            shortDescription = R.string.distortion_13_short_desc,
            longDescription = R.string.distortion_13_long_desc,
            examples = R.array.distortion_13_examples,
            iconResId = drawable.ic_distortion_13
        ),
    )

    override fun getAll(): Flow<List<DistortionStore>> {
        return flowOf(cognitiveDistortions)
    }

    override fun getById(id: Long): Flow<DistortionStore> {
        return flowOf(cognitiveDistortions.first { it.id == id })
    }

}
