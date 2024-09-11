package com.ainsln.core.data.util

import com.ainsln.core.datastore.model.DistortionStore
import com.ainsln.core.model.Distortion

internal fun DistortionStore.toDistortion(resourceManager: ResourceManager): Distortion = Distortion(
    id = id,
    name = resourceManager.getString(name),
    shortDescription = resourceManager.getString(shortDescription),
    longDescription = resourceManager.getString(longDescription),
    examples = resourceManager.getStringArray(examples).toList(),
    iconResId = iconResId
)
