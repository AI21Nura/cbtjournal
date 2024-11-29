package com.ainsln.core.data.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

public class BaseResourceManager @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceManager {

    override fun getString(stringId: Int, vararg formatArgs: Any): String {
        return context.getString(stringId, *formatArgs)
    }

    override fun getStringArray(arrayId: Int): Array<String> {
        return context.resources.getStringArray(arrayId)
    }
}
