package com.ainsln.core.data.util

import androidx.annotation.ArrayRes
import androidx.annotation.StringRes

public interface ResourceManager {

    public fun getString(@StringRes stringId: Int): String

    public fun getStringArray(@ArrayRes arrayId: Int): Array<String>

}
