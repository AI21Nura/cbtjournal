package com.ainsln.core.data.util

import android.content.Context
import com.ainsln.core.datastore.utils.AppLocale
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

public interface AppLocaleManager {
    public fun getLocale(): AppLocale
}

public class BaseAppLocaleManager @Inject constructor(
    @ApplicationContext private val context: Context
) : AppLocaleManager {

    override fun getLocale(): AppLocale {
        return when(getLocaleCode()){
            AppLocale.English.code -> AppLocale.English
            AppLocale.Russian.code -> AppLocale.Russian
            else -> AppLocale.English
        }
    }

    private fun getLocaleCode(): String {
        val locales = context.resources.configuration.locales

        return try {
            locales[0].language
        } catch (e: Throwable){
            AppLocale.English.code
        }
    }
}
