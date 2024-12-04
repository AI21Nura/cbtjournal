package com.ainsln.core.data.util

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

public interface ResourceManager {
    public fun getString(@StringRes stringId: Int, vararg formatArgs: Any): String
    public fun getStringArray(@ArrayRes arrayId: Int): Array<String>
}

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
